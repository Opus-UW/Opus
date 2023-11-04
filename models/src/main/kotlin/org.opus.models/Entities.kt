package org.opus.models

import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.json
import org.opus.models.Tasks.nullable
import org.opus.models.UserEntity.Companion.referrersOn
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

object Tasks : IntIdTable(name = "tasks", columnName = "task_id") {
    val completed = bool("completed")
    val action = text("action")
    val creationDate = text("creation_date")
    val dueDate = text("due_date").nullable()

    val userId = reference("user_id", Users)
}
class TaskEntity(id: EntityID<Int>) : IntEntity(id) {
    var completed by Tasks.completed
    var action by Tasks.action
    var creationDate by Tasks.creationDate
    var dueDate by Tasks.dueDate

    var userId by UserEntity referencedOn Tasks.userId


    companion object : IntEntityClass<TaskEntity>(Tasks)
}

object Notes : IntIdTable(name = "notes", columnName = "note_id") {
    val title = text("title")
    val body = text("body")
    val userId = reference("user_id", Users)
}

object Tags : IntIdTable(name = "tags", columnName = "tag_id") {
    val title = text("title")
    val colour = json<Colour>("colour", Json)
    val userId = reference("user_id", Users)
}

object Users : IntIdTable(name = "users", columnName = "user_id")
class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)
}

object TaskTags : Table(name = "task_tags") {
    val taskId = reference("task_id", Tasks)
    val tagId = reference("tag_id", Tags)
}

object NoteTags : Table(name = "note_tags") {
    val noteId = reference("note_id", Notes)
    val tagId = reference("tag_id", Tags)
}