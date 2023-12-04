package com.server.opus.util

import com.google.api.client.util.DateTime
import com.google.api.services.tasks.model.Task
import com.google.api.services.tasks.model.TaskList
import com.google.api.services.tasks.model.TaskLists
import com.google.api.services.tasks.model.Tasks
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.OAuth2CredentialsWithRefresh
import com.server.opus.plugins.configureRouting
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkConstructor
import org.models.opus.dao.DatabaseFactory
import java.util.*

fun Application.setup() {
    DatabaseFactory.init()
    DatabaseFactory.resetDatabase()
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
}

data class TaskParams(
    val id: String = "a",
    val title: String = "a",
    val due: Long = 0,
    val hidden: Boolean = false,
    val status: String = "needsAction"
)

fun mockGoogle(tasksOutput: List<TaskParams> = listOf()) {
    mockkConstructor(OAuth2CredentialsWithRefresh::class)
    every { anyConstructed<OAuth2CredentialsWithRefresh>().refreshAccessToken() } returns AccessToken.newBuilder()
        .setTokenValue("a").setExpirationTime(
            Date(0)
        ).build()
    mockkConstructor(com.google.api.services.tasks.Tasks.Tasklists.List::class)
    every { anyConstructed<com.google.api.services.tasks.Tasks.Tasklists.List>().execute().items[0].id } returns "0"
    mockkConstructor(com.google.api.services.tasks.Tasks.TasksOperations.List::class)
    every { anyConstructed<com.google.api.services.tasks.Tasks.TasksOperations.List>().execute() } returns Tasks().apply {
        items = tasksOutput.map {
            Task().apply {
                id = it.id
                title = it.title
                due = DateTime(it.due).toStringRfc3339()
                hidden = it.hidden
                status = it.status

            }
        }
    }
}