package org.models.opus.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Notes : IntIdTable(name = "notes", columnName = "note_id") {
    val title = text("title")
    val body = text("body")
    val userId = reference("user_id", Users)
}

class NoteEntity(id: EntityID<Int>) : IntEntity(id) {

    var title by Notes.title
    var body by Notes.body
    var tags by TagEntity via NoteTags

    var user by UserEntity referencedOn Notes.userId


    companion object : IntEntityClass<NoteEntity>(Notes)
}