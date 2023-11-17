package org.models.opus.db

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.models.opus.db.Users.entityId

object Tasks : IdTable<String>(name = "tasks") {
    val completed = bool("completed")
    val action = text("action")
    val creationDate = text("creation_date")
    val dueDate = text("due_date").nullable()

    val userId = reference("user_id", Users)


    override val id: Column<EntityID<String>> = Users.text("task_id").entityId()
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
}

class TaskEntity(id: EntityID<String>) : Entity<String>(id) {
    var completed by Tasks.completed
    var action by Tasks.action
    var creationDate by Tasks.creationDate
    var dueDate by Tasks.dueDate
    var tags by TagEntity via TaskTags

    var user by UserEntity referencedOn Tasks.userId


    companion object : EntityClass<String, TaskEntity>(Tasks)
}