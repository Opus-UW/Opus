package org.models.opus.db

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.json.json
import org.models.opus.models.Colour

object Tags : IntIdTable(name = "tags", columnName = "tag_id") {
    val title = text("title")
    val colour = integer("colour")
    val userId = reference("user_id", Users)
}

class TagEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TagEntity>(Tags)

    var title by Tags.title
    var colour by Tags.colour
    var user by UserEntity referencedOn Tags.userId

}