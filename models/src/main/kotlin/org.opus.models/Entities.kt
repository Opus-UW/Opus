package org.opus.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class Task(
    val completed: Boolean,
    val action: String,
    val date: LocalDateTime,
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