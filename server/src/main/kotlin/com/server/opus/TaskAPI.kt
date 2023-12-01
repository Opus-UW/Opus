package com.server.opus

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.tasks.Tasks
import com.google.api.services.tasks.model.Task
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

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

    fun modifiedTasks(): com.google.api.services.tasks.model.Tasks {
        val time = Clock.System.now()

        return taskService.tasks().list(taskListId)
            .setShowDeleted(true)
            .setShowHidden(true)
            .setShowCompleted(true)
            .setUpdatedMin(DateTime((time - 3.seconds).toString()).toStringRfc3339())
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