package com.server.opus

import com.server.opus.plugins.configureRouting
import com.server.opus.plugins.configureSockets
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.websocket.*
import org.models.opus.dao.DatabaseFactory
import java.time.Duration

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(WebSockets
    ) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    DatabaseFactory.init()
    configureRouting()
    configureSockets()
}
