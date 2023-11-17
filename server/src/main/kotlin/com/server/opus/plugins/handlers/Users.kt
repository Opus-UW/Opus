package com.server.opus.plugins.handlers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.models.opus.dao.dao

fun Routing.handleUsers() {
    post("/users/{user_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            val user = dao.user(userId)

            if(user == null){
                call.respond(dao.addNewUser(userId))
            } else {
                call.respond(user)
            }
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
}