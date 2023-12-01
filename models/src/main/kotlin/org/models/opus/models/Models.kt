package org.models.opus.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class Task(
    val completed: Boolean,
    val action: String,
    val creationDate: LocalDateTime,
    val dueDate: LocalDateTime?,
    val tags: List<Tag> = listOf(),
    val important: Boolean,
    val id: Int = -1
)


@Serializable
data class Note(
    val title: String, val body: String, val tags: List<Tag> = listOf(), val pinned: Boolean, val id: Int = -1
)

@Serializable
data class Tag(
    val title: String, val colour: Colour, val id: Int = -1
)


@Serializable
data class Colour(
    val red: Int, val green: Int, val blue: Int, val placeholder: Boolean = false
)

@Serializable
data class User(
    val id: String = ""
)


@Serializable
data class UserWSData(
    val userId: String, val accessToken: String
)


@Serializable
data class UserLoginData(
    val userId: String, val accessToken: String
)