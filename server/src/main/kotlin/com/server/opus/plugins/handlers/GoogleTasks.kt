package com.server.opus.plugins.handlers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Routing.handleTaskApi() {
    get("/tasks") {
        try {
//            val tasks = TaskAPI.tasks()
//            println(tasks)
            call.response.status(HttpStatusCode.OK)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}