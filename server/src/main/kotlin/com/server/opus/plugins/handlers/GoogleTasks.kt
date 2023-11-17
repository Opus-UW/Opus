package com.server.opus.plugins.handlers

import com.server.opus.CalendarAPI
import com.server.opus.TaskAPI
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.models.opus.dao.dao
import org.models.opus.models.Tag

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