package org.models.opus.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Tasks : IntIdTable(name = "tasks", columnName = "task_id") {
    val completed = bool("completed")
    val action = text("action")
    val creationDate = text("creation_date")
    val dueDate = text("due_date").nullable()

    val important = bool("important")
    val pinned = bool("pinned")

    val gTaskId = text("g_task_id").nullable()

    val userId = reference("user_id", Users)


}

class TaskEntity(id: EntityID<Int>) : IntEntity(id) {
    var completed by Tasks.completed
    var action by Tasks.action
    var creationDate by Tasks.creationDate
    var dueDate by Tasks.dueDate
    var important by Tasks.important
    var pinned by Tasks.pinned
    var tags by TagEntity via TaskTags

    var gTaskId by Tasks.gTaskId

    var user by UserEntity referencedOn Tasks.userId


    companion object : IntEntityClass<TaskEntity>(Tasks)
}