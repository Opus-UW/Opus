package org.opus.models

import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.json
import java.time.DateTimeException


@Serializable
data class Task(
    val completed: Boolean,
    val action: String,
    val creationDate: LocalDateTime,
    val dueDate: LocalDateTime?,
    val tags: List<Tag> = listOf(),
    val id: Int = -1
)


@Serializable
data class Note(
    val title: String,
    val body: String,
    val tags: List<Tag> = listOf(),
    val id: Int = -1
)

@Serializable
data class Tag(
    val title: String,
    val colour: Colour,
    val id: Int = -1
)


@Serializable
data class Colour(
    val red: Int,
    val green: Int,
    val blue: Int
)

object Tasks : IntIdTable(name="tasks", columnName = "task_id") {
    val completed = bool("completed")
    val action = text("action")
    val creationDate = text("creation_date")
    val dueDate = text("due_date").nullable()

    val userId = reference("user_id", Users)
}

object Notes : IntIdTable(name="notes", columnName = "note_id") {
    val title = text("title")
    val body = text("body")
    val userId = reference("user_id", Users)
}

object Tags: IntIdTable(name="tags", columnName = "tag_id") {
    val title = text("title")
    val colour = json<Colour>("colour", Json)
    val userId = reference("user_id", Users)
}

object Users: IntIdTable(name="users", columnName = "user_id")

object TaskTags: Table(name="task_tags"){
    val taskId = reference("task_id", Tasks)
    val tagId = reference("tag_id", Tags)
}

object NoteTags: Table(name="note_tags"){
    val noteId = reference("note_id", Notes)
    val tagId = reference("tag_id", Tags)
}