package com.server.opus

import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest
import com.google.api.client.googleapis.auth.oauth2.OAuth2Utils
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.oauth2.Oauth2Scopes
import com.google.api.services.tasks.Tasks
import com.google.api.services.tasks.TasksScopes
import com.google.api.services.tasks.model.Task
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.OAuth2CredentialsWithRefresh
import com.google.auth.oauth2.OAuth2CredentialsWithRefresh.OAuth2RefreshHandler
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.annotations.VisibleForTesting
import org.models.opus.dao.dao
import org.models.opus.models.DBCredentials
import org.models.opus.models.User
import java.io.FileNotFoundException
import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TaskAPI(private val user: User) {

    private val APPLICATION_NAME = "Opus"
    private val credentials: OAuth2CredentialsWithRefresh

    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    private val CREDENTIALS_FILE_PATH = "/credentials.json"

    // If modifying these scopes, delete your previously saved tokens/ folder.

    private val taskService: Tasks

    private val taskListId: String

    private val handler = OAuth2RefreshHandler {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val credFile = object{}.javaClass.getResource(CREDENTIALS_FILE_PATH) ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
        val installedObj = Json.parseToJsonElement(credFile.readText()).jsonObject.get("installed")!!.jsonObject
        val SCOPES = listOf(TasksScopes.TASKS, GmailScopes.GMAIL_COMPOSE) + Oauth2Scopes.all()
        println(installedObj)

        val newToken = GoogleRefreshTokenRequest(httpTransport, JSON_FACTORY, user.credentials.refreshToken, installedObj.get("client_id")!!.jsonPrimitive.content, installedObj.get("client_secret")!!.jsonPrimitive.content).setScopes(SCOPES).execute()

        AccessToken(newToken.accessToken, Date(newToken.expiresInSeconds*1000))
    }

    init {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        val accessToken = AccessToken.newBuilder().setTokenValue(user.credentials.accessToken).setExpirationTime(Date(user.credentials.expirationTimeMilliseconds)).build()
        credentials = OAuth2CredentialsWithRefresh.newBuilder().setAccessToken(accessToken).setRefreshHandler(handler).build()
        val newToken = credentials.refreshAccessToken()

        runBlocking {
            dao.editUser(user.id, user.credentials.copy(accessToken=newToken.tokenValue, expirationTimeMilliseconds = newToken.expirationTime.time))
        }

        taskService = Tasks.Builder(httpTransport, JSON_FACTORY, HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME)
            .build()

        taskListId = taskService.tasklists().list().execute().items[0].id
    }

    fun allTasks(): com.google.api.services.tasks.model.Tasks{
        return taskService.tasks().list(taskListId)
            .setShowDeleted(true)
            .setShowHidden(true)
            .setShowCompleted(true)
            .execute()
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