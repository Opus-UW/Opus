package org.opus.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class Task(val completed: Boolean,
                val action: String,
                val date: LocalDateTime,
                val tags: List<String> = listOf<String>(),
                val id: String = "")