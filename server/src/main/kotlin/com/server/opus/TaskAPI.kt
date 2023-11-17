package com.server.opus

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.Events
import com.google.api.services.tasks.Tasks
import com.google.api.services.tasks.TasksScopes
import com.google.api.services.tasks.model.Task
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader

class TaskAPI(private val accessTokenString: String) {

    private val APPLICATION_NAME = "Opus"
    private val accessToken = AccessToken.newBuilder().setTokenValue(accessTokenString).build()

    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    // If modifying these scopes, delete your previously saved tokens/ folder.

    private val taskService: Tasks

    init {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        val credential = GoogleCredentials.create(accessToken)

        taskService = Tasks.Builder(httpTransport, JSON_FACTORY, HttpCredentialsAdapter(credential))
            .setApplicationName(APPLICATION_NAME)
            .build()
    }

    fun tasks(maxResults: Int = 100, timeMinimum: DateTime = DateTime(System.currentTimeMillis()), orderBy: String = "startTime"): com.google.api.services.tasks.model.Tasks {
        println(taskService.tasklists().list().execute())

        return taskService.tasks().list("MDU5MjcxNDc3MDc4NDg0Mjc3MjM6MDow")
            .setMaxResults(maxResults)
            .execute()
    }

    fun patchEvent(task: Task) {
        taskService.tasks().patch("MDU5MjcxNDc3MDc4NDg0Mjc3MjM6MDow", task.id, task).execute()
    }
}