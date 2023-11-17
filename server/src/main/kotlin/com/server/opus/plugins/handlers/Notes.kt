package com.server.opus.plugins.handlers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.models.opus.dao.dao
import org.models.opus.models.Note

fun Routing.handleNotes() {
    get("/users/{user_id}/notes") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            call.respond(dao.userNotes(userId))
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    post("/users/{user_id}/notes") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            val note = call.receive<Note>()
            dao.addNewNote(note.title, note.body, note.tags, userId)
            call.respond(dao.userNotes(userId))
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    delete("/users/{user_id}/notes/{note_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            val noteId = call.parameters.getOrFail("note_id").toInt()

            dao.deleteNote(noteId)

            call.respond(dao.userNotes(userId))

        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    put("/users/{user_id}/notes/{note_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            val noteId = call.parameters.getOrFail("note_id").toInt()

            val note = call.receive<Note>()

            dao.editNote(noteId, note.title, note.body, note.tags)

            call.respond(dao.userNotes(userId))
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
}