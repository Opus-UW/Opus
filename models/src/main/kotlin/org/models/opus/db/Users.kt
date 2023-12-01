package org.models.opus.db

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.json.json
import org.models.opus.models.DBCredentials

object Users : IdTable<String>(name = "users") {
    val credentials = json<DBCredentials>("credentials", Json)
    override val id: Column<EntityID<String>> = text("user_id").entityId()
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
}
class UserEntity(id: EntityID<String>) : Entity<String>(id) {
    var credentials by Users.credentials
    companion object : EntityClass<String, UserEntity>(Users)
}