package org.opus.data

import org.opus.models.Colour
import org.opus.models.Note
import org.opus.models.Tag
import org.opus.models.Task

interface DAOFacade {
    suspend fun allTasks(): List<Task>
    suspend fun task(id: Int): Task?
    suspend fun userTasks(userId: Int): List<Task>
    suspend fun addNewTask(completed: Boolean, action: String, creationDate: String, dueDate: String?, userId: Int): Task?
    suspend fun editTask(id: Int, completed: Boolean, action: String, creationDate: String, dueDate: String?): Boolean
    suspend fun deleteTask(id: Int): Boolean

    suspend fun allNotes(): List<Note>
    suspend fun note(id: Int): Note?
    suspend fun userNotes(userId: Int): List<Note>
    suspend fun addNewNote(title: String, body: String, userId: Int): Note?
    suspend fun editNote(id: Int, title: String, body: String): Boolean
    suspend fun deleteNote(id: Int): Boolean

    suspend fun allTags(): List<Tag>
    suspend fun tag(id: Int): Tag?
    suspend fun userTags(userId: Int): List<Tag>
    suspend fun addNewTag(title: String, colour: Colour, userId: Int): Tag?
    suspend fun editTag(id: Int, title: String, colour: Colour): Boolean
    suspend fun deleteTag(id: Int): Boolean

    suspend fun getTaskTags(id:Int): List<Tag>
    suspend fun getNoteTags(id:Int): List<Tag>
    suspend fun linkTaskToTag(taskId:Int, tagId: Int): Boolean
    suspend fun linkNoteToTag(noteId:Int, tagId: Int): Boolean
    suspend fun unlinkTaskToTag(taskId:Int, tagId: Int): Boolean
    suspend fun unlinkNoteToTag(noteId:Int, tagId: Int): Boolean
}