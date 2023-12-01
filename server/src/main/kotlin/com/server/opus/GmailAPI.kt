package com.server.opus

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.model.Message
import com.google.api.services.oauth2.Oauth2
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import com.server.opus.CreateEmail.createEmail
import com.server.opus.CreateMessage.createMessageWithEmail
import java.io.IOException
import javax.mail.MessagingException

/* Class to demonstrate the use of Gmail Send Message API */
class GmailAPI(private val accessTokenString: String) {
    private val APPLICATION_NAME = "Opus"
    private val accessToken: AccessToken

    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
    private val gmailService: Gmail

    private val fromEmailAddress: String
    private val toEmailAddress: String

    init {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        accessToken = AccessToken.newBuilder().setTokenValue(accessTokenString).build()

        val credential = GoogleCredentials.create(accessToken)
        val requestInitializer: HttpRequestInitializer = HttpCredentialsAdapter(credential)

        gmailService = Gmail.Builder(
            /*NetHttpTransport(),*/
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


    // If modifying these scopes, delete your previously saved tokens/ folder.

    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param fromEmailAddress - Email address to appear in the from: header
     * @param toEmailAddress   - Email address of the recipient
     * @return the sent message, `null` otherwise.
     * @throws MessagingException - if a wrongly formatted address is encountered.
     * @throws IOException        - if service account credentials file not found.
     */
    @Throws(MessagingException::class, IOException::class)
    fun sendEmail(
        messageSubject: String,
        bodyText: String
    ): Message? {

        // Encode as MIME message
        /*val props = Properties()
        val session = Session.getDefaultInstance(props, null)
        val email = MimeMessage(session)
        email.setFrom(InternetAddress(fromEmailAddress))
        email.addRecipient(
            javax.mail.Message.RecipientType.TO,
            InternetAddress(toEmailAddress)
        )
        email.subject = messageSubject
        email.setText(bodyText)*/

        val email = createEmail(fromEmailAddress, toEmailAddress, messageSubject, bodyText)

        // Encode and wrap the MIME message into a gmail message
        /*val buffer = ByteArrayOutputStream()
        email.writeTo(buffer)
        val rawMessageBytes = buffer.toByteArray()
        val encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes)
        var message = Message()
        message.setRaw(encodedEmail)*/
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