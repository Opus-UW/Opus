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
    private val accessToken: AccessToken

    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    // If modifying these scopes, delete your previously saved tokens/ folder.

    private val taskService: Tasks

    private val taskListId: String

    init {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        accessToken = AccessToken.newBuilder().setTokenValue(accessTokenString).build()

        val credential = GoogleCredentials.create(accessToken)

        taskService = Tasks.Builder(httpTransport, JSON_FACTORY, HttpCredentialsAdapter(credential))
            .setApplicationName(APPLICATION_NAME)
            .build()

        taskListId = taskService.tasklists().list().execute().items[0].id

    }

    fun tasks(
        maxResults: Int = 100,
        timeMinimum: DateTime = DateTime(System.currentTimeMillis()),
        orderBy: String = "startTime"
    ): com.google.api.services.tasks.model.Tasks {

        return taskService.tasks().list("MDU5MjcxNDc3MDc4NDg0Mjc3MjM6MDow")
            .setMaxResults(maxResults)
            .execute()
    }

    fun patchTask(task: Task): Task {
        return taskService.tasks().patch(taskListId, task.id, task).execute()
    }

    fun createTask(task: Task): Task {
        return taskService.tasks().insert(taskListId, task).execute()
    }

    fun deleteTask(taskId: String) {
        taskService.tasks().delete(taskListId, taskId).execute()
    }
}