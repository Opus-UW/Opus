package org.models.opus.dao

import org.models.opus.models.*

interface DAOFacade {
    suspend fun allTasks(): List<Task>
    suspend fun task(id: Int): Task?

    suspend fun unsentTasks(): List<Task>
    suspend fun taskGId(id: Int): String?
    suspend fun userTasks(userId: String): List<Task>
    suspend fun addNewTask(
        completed: Boolean, action: String, creationDate: String, dueDate: String?, tags: List<Tag>, notificationSent: Boolean, important: Boolean, gTaskId: String?, userId: String
    ): Task

    suspend fun addNewGTask(
        completed: Boolean, action: String, dueDate: String, gTaskId: String?, userId: String
    ): Task

    suspend fun editTask(
        id: Int, completed: Boolean, action: String, creationDate: String, dueDate: String?, tags: List<Tag>, notificationSent: Boolean, important: Boolean,  gTaskId: String?
    ): Boolean

    suspend fun editGTask(
        gTaskId: String, completed: Boolean, action: String, dueDate: String?
    ): Boolean
    suspend fun deleteTask(id: Int): Boolean
    suspend fun deleteGTask(gTaskId: String): Boolean

    suspend fun allNotes(): List<Note>
    suspend fun note(id: Int): Note?
    suspend fun userNotes(userId: String): List<Note>
    suspend fun addNewNote(title: String, body: String, tags: List<Tag>, pinned: Boolean, userId: String): Note
    suspend fun editNote(id: Int, title: String, body: String, tags: List<Tag>, pinned: Boolean): Boolean
    suspend fun deleteNote(id: Int): Boolean

    suspend fun allTags(): List<Tag>
    suspend fun tag(id: Int): Tag?
    suspend fun userTags(userId: String): List<Tag>
    suspend fun addNewTag(title: String, colour: Colour, userId: String): Tag
    suspend fun editTag(id: Int, title: String, colour: Colour): Boolean
    suspend fun deleteTag(id: Int): Boolean


    suspend fun allUsers(): List<User>
    suspend fun user(id: String): User?
    suspend fun addNewUser(id: String): User
    suspend fun editUser(id: String): Boolean
    suspend fun deleteUser(id: String): Boolean
}