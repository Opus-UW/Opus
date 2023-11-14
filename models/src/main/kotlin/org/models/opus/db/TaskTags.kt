package org.models.opus.db

import org.jetbrains.exposed.sql.Table

object TaskTags : Table(name = "task_tags") {
    val taskId = reference("task_id", Tasks)
    val tagId = reference("tag_id", Tags)
}