package com.server.opus

import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Message
import com.google.api.services.oauth2.Oauth2
import com.google.api.services.oauth2.Oauth2Scopes
import com.google.api.services.tasks.TasksScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.OAuth2CredentialsWithRefresh
import com.server.opus.CreateEmail.createEmail
import com.server.opus.CreateMessage.createMessageWithEmail
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.models.opus.models.DBCredentials
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*
import javax.mail.MessagingException

class GmailAPI(private val creds: DBCredentials) {
    private val APPLICATION_NAME = "Opus"
    private val credentials: OAuth2CredentialsWithRefresh

    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
    private val gmailService: Gmail
    private val CREDENTIALS_FILE_PATH = "/credentials.json"
    private val fromEmailAddress: String
    private val toEmailAddress: String

    private val handler = OAuth2CredentialsWithRefresh.OAuth2RefreshHandler {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val credFile = object {}.javaClass.getResource(CREDENTIALS_FILE_PATH)
            ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
        val installedObj = Json.parseToJsonElement(credFile.readText()).jsonObject.get("installed")!!.jsonObject
        val SCOPES = listOf(TasksScopes.TASKS, GmailScopes.GMAIL_COMPOSE) + Oauth2Scopes.all()
        println(installedObj)

        val newToken = GoogleRefreshTokenRequest(
            httpTransport,
            JSON_FACTORY,
            creds.refreshToken,
            installedObj.get("client_id")!!.jsonPrimitive.content,
            installedObj.get("client_secret")!!.jsonPrimitive.content
        ).setScopes(SCOPES).execute()

        AccessToken(newToken.accessToken, Date(newToken.expiresInSeconds * 1000))
    }
    init {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        val accessToken = AccessToken.newBuilder().setTokenValue(creds.accessToken).setExpirationTime(Date(creds.expirationTimeMilliseconds)).build()
        credentials = OAuth2CredentialsWithRefresh.newBuilder().setAccessToken(accessToken).setRefreshHandler(handler).build()
        credentials.refreshAccessToken()

        val credential = GoogleCredentials.create(accessToken)
        val requestInitializer: HttpRequestInitializer = HttpCredentialsAdapter(credential)

        gmailService = Gmail.Builder(
            httpTransport,
            JSON_FACTORY,
            requestInitializer
        )
            .setApplicationName(APPLICATION_NAME)
            .build()

        val oauth2 = Oauth2.Builder(httpTransport, JSON_FACTORY, requestInitializer).build()

        fromEmailAddress = oauth2.userinfo().get().execute().email
        toEmailAddress = oauth2.userinfo().get().execute().email

    }

    @Throws(MessagingException::class, IOException::class)
    fun sendEmail(
        messageSubject: String,
        bodyText: String
    ): Message? {
        val email = createEmail(fromEmailAddress, toEmailAddress, messageSubject, bodyText)
        var message = createMessageWithEmail(email)
        try {
            // Create send message
            message = gmailService.users().messages().send("me", message).execute()
            println("Message id: " + message.id)
            println(message.toPrettyString())
            return message
        } catch (e: GoogleJsonResponseException) {
            // TODO(developer) - handle error appropriately
            val error = e.details
            if (error.code == 403) {
                System.err.println("Unable to send message: " + e.details)
            } else {
                throw e
            }
        }
        return null
    }
}