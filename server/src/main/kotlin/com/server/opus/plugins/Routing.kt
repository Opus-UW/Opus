package com.server.opus.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.opus.models.Todo

val todosMap = mutableMapOf<Int, MutableList<Todo>>()

fun Application.configureRouting() {
    routing {
        get("/users/{user_id}/todos") {
            val userId = call.parameters["user_id"]?.toInt()
            if(userId == null) {
                call.response.status(HttpStatusCode.BadRequest)
                return@get
            }
            val todos = todosMap[userId]

            if (todos != null) {
                call.respond(todos)
            } else {
                call.response.status(HttpStatusCode.BadRequest)
            }
        }
        post("/users/{user_id}/todos") {
            val userId = call.parameters["user_id"]?.toInt()

            if(userId == null) {
                call.response.status(HttpStatusCode.BadRequest)
                return@post
            }

            val todos = todosMap[userId]
            val todo = call.receive<Todo>()

            if (todos != null) {
                todos.add(todo)
            } else {
                todosMap[userId] = mutableListOf(todo)
            }

            call.response.status(HttpStatusCode.OK)


        }
        delete("/users/{user_id}/todos/{todo_id}") {
            val userId = call.parameters["user_id"]?.toInt()
            val todoId = call.parameters["todo_id"]?.toInt()
            if(userId == null || todoId == null) {
                call.response.status(HttpStatusCode.BadRequest)
                return@delete
            }

            val todos = todosMap[userId]

            if (todos != null && todoId < todos.size) {
                todos.removeAt(todoId)
                call.response.status(HttpStatusCode.OK)
            } else {
                call.response.status(HttpStatusCode.BadRequest)
            }
        }
    }
}
