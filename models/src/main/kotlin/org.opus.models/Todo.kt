package org.opus.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class Todo(val completed: Boolean, val action: String, val date: LocalDateTime)