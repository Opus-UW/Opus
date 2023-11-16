package com.server.opus.plugins

import com.server.opus.plugins.handlers.*
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    routing {
        handleTasks()
        handleNotes()
        handleTags()
        handleCalendarApi()
        handleTaskApi()
    }
}
