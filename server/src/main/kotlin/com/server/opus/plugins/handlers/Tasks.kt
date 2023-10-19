package com.server.opus.plugins.handlers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.opus.data.DataClient
import org.opus.models.Task

fun Routing.handleTasks() {
    val dataClient = DataClient.getInstance()
    get("/users/{user_id}/tasks") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            call.respond(dataClient.getTasks(userId))
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    get("/users/{user_id}/uncompleted-tasks") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            call.respond(dataClient.getTasks(userId).filter { !it.completed })
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    get("/users/{user_id}/completed-todos") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            call.respond(dataClient.getTasks(userId).filter { it.completed })
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    post("/users/{user_id}/tasks") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val task = call.receive<Task>()
            dataClient.addTask(userId, task)
            call.respond(dataClient.getTasks(userId))
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    delete("/users/{user_id}/tasks/{task_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val taskId = call.parameters.getOrFail("task_id").toInt()

            dataClient.deleteTask(userId, taskId)

            call.respond(dataClient.getTasks(userId))

        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    put("/users/{user_id}/tasks/{task_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val taskId = call.parameters.getOrFail("task_id").toInt()

            val task = call.receive<Task>()

            dataClient.updateTask(userId, taskId, task)

            call.respond(dataClient.getTasks(userId))
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
}