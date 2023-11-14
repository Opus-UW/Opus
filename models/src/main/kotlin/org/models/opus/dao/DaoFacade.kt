package org.models.opus.dao

import org.models.opus.models.Colour
import org.models.opus.models.Note
import org.models.opus.models.Tag
import org.models.opus.models.Task

interface DAOFacade {
    suspend fun allTasks(): List<Task>
    suspend fun task(id: Int): Task?
    suspend fun userTasks(userId: Int): List<Task>
    suspend fun addNewTask(
        completed: Boolean, action: String, creationDate: String, dueDate: String?, tags: List<Tag>, userId: Int
    ): Task

    suspend fun editTask(
        id: Int, completed: Boolean, action: String, creationDate: String, dueDate: String?, tags: List<Tag>
    ): Boolean

    suspend fun deleteTask(id: Int): Boolean

    suspend fun allNotes(): List<Note>
    suspend fun note(id: Int): Note?
    suspend fun userNotes(userId: Int): List<Note>
    suspend fun addNewNote(title: String, body: String, tags: List<Tag>, userId: Int): Note
    suspend fun editNote(id: Int, title: String, body: String, tags: List<Tag>): Boolean
    suspend fun deleteNote(id: Int): Boolean

    suspend fun allTags(): List<Tag>
    suspend fun tag(id: Int): Tag?
    suspend fun userTags(userId: Int): List<Tag>
    suspend fun addNewTag(title: String, colour: Colour, userId: Int): Tag
    suspend fun editTag(id: Int, title: String, colour: Colour): Boolean
    suspend fun deleteTag(id: Int): Boolean
}