package com.server.opus.util

import kotlinx.datetime.LocalDateTime
import org.models.opus.models.*

val DefaultNote = Note("", "", listOf(), false)
val DefaultTag = Tag("", Colour.PEACH)
val DefaultTask = Task(false, "", LocalDateTime(1, 1, 1, 1, 1, 1), null, listOf(), false, false)
val DefaultUser = User(DBCredentials(0, "", ""), "0")
