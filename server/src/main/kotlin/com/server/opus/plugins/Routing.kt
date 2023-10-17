package com.server.opus.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.opus.models.Todo
import java.util.UUID

val todosMap = mutableMapOf<Int, MutableMap<String,Todo>>()

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
                call.respond(todos.values.toList())
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
            val newId = UUID.randomUUID().toString()
            val newTodo = todo.copy(id = newId)

            if (todos != null) {
                todos[newId] = newTodo
            } else {
                todosMap[userId] = mutableMapOf(Pair(newId, newTodo))
            }

            call.respond(todosMap[userId]!!.values.toList())


        }
        delete("/users/{user_id}/todos/{todo_id}") {
            val userId = call.parameters["user_id"]?.toInt()
            val todoId = call.parameters["todo_id"]
            if(userId == null || todoId == null) {
                call.response.status(HttpStatusCode.BadRequest)
                return@delete
            }

            val todos = todosMap[userId]

            if (todos != null && todos.contains(todoId)) {
                todos.remove(todoId)
                call.respond(todos.values.toList())
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

            val todos = todosMap[userId]
            val todo = call.receive<Todo>()

            if (todos != null && todos.contains(todoId)) {
                todos[todoId] = todo
                call.respond(todos.values.toList())
            } else {
                call.response.status(HttpStatusCode.BadRequest)
            }
        }
    }
}
