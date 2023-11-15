package com.server.opus

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import io.ktor.events.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.security.GeneralSecurityException
import java.util.*


/* class to demonstrate use of Calendar events list API */
object CalendarQuickstart {
    /**
     * Application name.
     */
    private const val APPLICATION_NAME = "Google Calendar API Java Quickstart"

    /**
     * Global instance of the JSON factory.
     */
    private val JSON_FACTORY: com.google.api.client.json.JsonFactory = GsonFactory.getDefaultInstance()

    /**
     * Directory to store authorization tokens for this application.
     */
    private const val TOKENS_DIRECTORY_PATH = "tokens"

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private val SCOPES = listOf<String>(CalendarScopes.CALENDAR_READONLY)
    private const val CREDENTIALS_FILE_PATH = "/credentials.json"

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    @Throws(IOException::class)
    private fun getCredentials(HTTP_TRANSPORT: com.google.api.client.http.javanet.NetHttpTransport): Credential {
        // Load client secrets.
        val `in` =
            CalendarQuickstart::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
                ?: throw FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH)
        val clientSecrets: GoogleClientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(`in`))

        // Build flow and trigger user authorization request.
        val flow: GoogleAuthorizationCodeFlow = Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
        )
            .setDataStoreFactory(
                com.google.api.client.util.store.FileDataStoreFactory(
                    File(
                        TOKENS_DIRECTORY_PATH
                    )
                )
            )
            .setAccessType("offline")
            .build()
        val receiver: LocalServerReceiver = Builder().setPort(8888).build()
        //returns an authorized Credential object.
        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    @Throws(IOException::class, GeneralSecurityException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        // Build a new authorized API client service.
        val HTTP_TRANSPORT: com.google.api.client.http.javanet.NetHttpTransport =
            GoogleNetHttpTransport.newTrustedTransport()
        val service: Calendar = Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build()

        // List the next 10 events from the primary calendar.
        val now: com.google.api.client.util.DateTime = com.google.api.client.util.DateTime(System.currentTimeMillis())
        val events: Events = service.events().list("primary")
            .setMaxResults(10)
            .setTimeMin(now)
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute()
        val items: List<Event> = events.getItems()
        if (items.isEmpty()) {
            println("No upcoming events found.")
        } else {
            println("Upcoming events")
            for (event in items) {
                var start: com.google.api.client.util.DateTime = event.getStart().getDateTime()
                if (start == null) {
                    start = event.getStart().getDate()
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start)
            }
        }
    }
}