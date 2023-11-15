package com.server.opus.plugins

import com.server.opus.plugins.handlers.handleCalendarApi
import com.server.opus.plugins.handlers.handleNotes
import com.server.opus.plugins.handlers.handleTags
import com.server.opus.plugins.handlers.handleTasks
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    routing {
        handleTasks()
        handleNotes()
        handleTags()
        handleCalendarApi()
    }
}
