package org.models.opus.models

import com.google.api.client.auth.oauth2.Credential
import kotlinx.datetime.*
import kotlinx.serialization.Serializable


@Serializable
data class Task(
    val completed: Boolean,
    val action: String,
    val creationDate: LocalDateTime,
    val dueDate: LocalDateTime?,
    val tags: List<Tag> = listOf(),
    val id: String = ""
)


@Serializable
data class Note(
    val title: String, val body: String, val tags: List<Tag> = listOf(), val id: Int = -1
)

@Serializable
data class Tag(
    val title: String, val colour: Colour, val id: Int = -1
)


@Serializable
data class Colour(
    val red: Int, val green: Int, val blue: Int
)

@Serializable
data class User(
    val id: String = ""
)

//@Serializable
//data class NERT(
//    val yert:
//)
