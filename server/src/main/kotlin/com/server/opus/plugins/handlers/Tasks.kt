package com.server.opus.plugins.handlers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.models.opus.dao.dao
import org.models.opus.models.Task

fun Routing.handleTasks() {
    get("/users/{user_id}/tasks") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            call.respond(dao.userTasks(userId))
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    get("/users/{user_id}/uncompleted-tasks") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            call.respond(dao.userTasks(userId).filter { !it.completed })
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    get("/users/{user_id}/completed-todos") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            call.respond(dao.userTasks(userId).filter { it.completed })
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    post("/users/{user_id}/tasks") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val task = call.receive<Task>()

            dao.addNewTask(
                task.completed, task.action, task.creationDate.toString(), task.dueDate?.toString(), task.tags, userId
            )

            call.respond(dao.userTasks(userId))
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    delete("/users/{user_id}/tasks/{task_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val taskId = call.parameters.getOrFail("task_id").toInt()

            dao.deleteTask(taskId)

            call.respond(dao.userTasks(userId))

        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    put("/users/{user_id}/tasks/{task_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val taskId = call.parameters.getOrFail("task_id").toInt()

            val task = call.receive<Task>()

            dao.editTask(
                taskId, task.completed, task.action, task.creationDate.toString(), task.dueDate?.toString(), task.tags
            )

            call.respond(dao.userTasks(userId))
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
}