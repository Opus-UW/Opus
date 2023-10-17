package com.server.opus.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.opus.models.Task
import java.util.UUID

val tasksMap = mutableMapOf<Int, MutableMap<String,Task>>()

fun Application.configureRouting() {
    routing {
        get("/users/{user_id}/tasks") {
            val userId = call.parameters["user_id"]?.toInt()
            if(userId == null) {
                call.response.status(HttpStatusCode.BadRequest)
                return@get
            }
            val tasks = tasksMap[userId]

            if (tasks != null) {
                call.respond(tasks.values.toList())
            } else {
                call.response.status(HttpStatusCode.BadRequest)
            }
        }
        get("/users/{user_id}/uncompleted-tasks") {
            val userId = call.parameters["user_id"]?.toInt()
            if(userId == null) {
                call.response.status(HttpStatusCode.BadRequest)
                return@get
            }
            val tasks = tasksMap[userId]

            if (tasks != null) {
                call.respond(tasks.filter{!it.value.completed}.values.toList())
            } else {
                call.response.status(HttpStatusCode.BadRequest)
            }
        }
        get("/users/{user_id}/completed-todos") {
            val userId = call.parameters["user_id"]?.toInt()
            if(userId == null) {
                call.response.status(HttpStatusCode.BadRequest)
                return@get
            }
            val tasks = tasksMap[userId]

            if (tasks != null) {
                call.respond(tasks.filter{it.value.completed}.values.toList())
            } else {
                call.response.status(HttpStatusCode.BadRequest)
            }
        }
        post("/users/{user_id}/tasks") {
            val userId = call.parameters["user_id"]?.toInt()

            if(userId == null) {
                call.response.status(HttpStatusCode.BadRequest)
                return@post
            }

            val tasks = tasksMap[userId]
            val task = call.receive<Task>()
            val newId = UUID.randomUUID().toString()
            val newTask = task.copy(id = newId)

            if (tasks != null) {
                tasks[newId] = newTask
            } else {
                tasksMap[userId] = mutableMapOf(Pair(newId, newTask))
            }

            call.respond(tasksMap[userId]!!.values.toList())


        }
        delete("/users/{user_id}/tasks/{task_id}") {
            val userId = call.parameters["user_id"]?.toInt()
            val taskId = call.parameters["task_id"]
            if(userId == null || taskId == null) {
                call.response.status(HttpStatusCode.BadRequest)
                return@delete
            }

            val tasks = tasksMap[userId]

            if (tasks != null && tasks.contains(taskId)) {
                tasks.remove(taskId)
                call.respond(tasks.values.toList())
            } else {
                call.response.status(HttpStatusCode.BadRequest)
            }
        }
        put("/users/{user_id}/todos/{todo_id}") {
            val userId = call.parameters["user_id"]?.toInt()
            val todoId = call.parameters["todo_id"]
            if(userId == null || todoId == null) {
                call.response.status(HttpStatusCode.BadRequest)
                return@put
            }

            val tasks = tasksMap[userId]
            val task = call.receive<Task>()

            if (tasks != null && tasks.contains(todoId)) {
                tasks[todoId] = task
                call.respond(tasks.values.toList())
            } else {
                call.response.status(HttpStatusCode.BadRequest)
            }
        }
    }
}
