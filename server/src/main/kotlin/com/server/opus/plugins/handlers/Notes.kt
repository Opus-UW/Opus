package com.server.opus.plugins.handlers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.opus.data.DataClient
import org.opus.models.Note

fun Routing.handleNotes() {
    val dataClient = DataClient.getInstance()
    get("/users/{user_id}/notes") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            call.respond(dataClient.getNotes(userId))
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    post("/users/{user_id}/notes") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val note = call.receive<Note>()
            dataClient.addNote(userId, note)
            call.respond(dataClient.getNotes(userId))
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    delete("/users/{user_id}/notes/{note_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val noteId = call.parameters.getOrFail("note_id").toInt()

            dataClient.deleteNote(userId, noteId)

            call.respond(dataClient.getNotes(userId))

        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    put("/users/{user_id}/notes/{note_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id").toInt()
            val noteId = call.parameters.getOrFail("note_id").toInt()

            val note = call.receive<Note>()

            dataClient.updateNote(userId, noteId, note)

            call.respond(dataClient.getNotes(userId))
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
}