package com.server.opus.plugins.handlers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.opus.data.DataClient
import org.opus.models.Tag

fun Routing.handleTags() {
    val dataClient = DataClient.getInstance()
    get("/users/{user_id}/tags") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            call.respond(dataClient.getTags(userId))
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    post("/users/{user_id}/tags") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val tag = call.receive<Tag>()
            dataClient.addTag(userId, tag)
            call.respond(dataClient.getTags(userId))
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    delete("/users/{user_id}/tags/{tag_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val tagId = call.parameters.getOrFail("tag_id").toInt()

            dataClient.deleteTag(userId, tagId)

            call.respond(dataClient.getTags(userId))

        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    put("/users/{user_id}/tags/{tag_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val tagId = call.parameters.getOrFail("tag_id").toInt()

            val tag = call.receive<Tag>()

            dataClient.updateTag(userId, tagId, tag)

            call.respond(dataClient.getTags(userId))
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
}