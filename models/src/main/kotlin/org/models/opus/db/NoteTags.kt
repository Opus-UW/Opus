package org.models.opus.db

import org.jetbrains.exposed.sql.Table

object NoteTags : Table(name = "note_tags") {
    val noteId = reference("note_id", Notes)
    val tagId = reference("tag_id", Tags)
}