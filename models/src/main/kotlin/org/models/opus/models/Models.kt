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
    val id: Int = -1
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
enum class Colour{
    RED,
    BROWN,
    ORANGE_YELLOW,
    GREEN,
    TEAL,
    CYAN,
    BLUE,
    PURPLE,
    PLUM_PEACH;
}


//Int to Enum
inline fun <reified T : Enum<T>> Int.toEnum(): T? {
    return enumValues<T>().firstOrNull { it.ordinal == this }
}



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