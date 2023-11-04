package com.server.opus.plugins.handlers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.models.opus.dao.dao
import org.models.opus.models.Tag

fun Routing.handleTags() {
    get("/users/{user_id}/tags") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            call.respond(dao.userTags(userId))
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    post("/users/{user_id}/tags") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val tag = call.receive<Tag>()
            dao.addNewTag(tag.title, tag.colour, userId)
            call.respond(dao.userTags(userId))
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    delete("/users/{user_id}/tags/{tag_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val tagId = call.parameters.getOrFail("tag_id").toInt()

            dao.deleteTag(tagId)

            call.respond(dao.userTags(userId))

        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    put("/users/{user_id}/tags/{tag_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val tagId = call.parameters.getOrFail("tag_id").toInt()

            val tag = call.receive<Tag>()

            dao.editTag(tagId, tag.title, tag.colour)

            call.respond(dao.userTags(userId))
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
}