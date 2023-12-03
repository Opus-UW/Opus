package com.server.opus.util

import com.server.opus.plugins.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.models.opus.dao.DatabaseFactory

fun Application.setup(){
    DatabaseFactory.init()
    DatabaseFactory.resetDatabase()
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
}