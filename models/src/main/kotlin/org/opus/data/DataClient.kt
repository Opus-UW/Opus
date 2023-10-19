package org.opus.data

import org.opus.models.Note
import org.opus.models.Tag
import org.opus.models.Task
import java.util.NoSuchElementException

class DataClient {
    companion object {
        private var instance: DataClient? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: DataClient().also { instance = it }
        }
    }

    val tasksMap = mutableMapOf<Int, MutableMap<Int, Task>>()
    private var taskIdCounter = 0

    fun getTasks(userId: Int): List<Task> {
        val tasks = tasksMap.getOrDefault(userId, mutableMapOf())

        return tasks.values.toList()
    }

    fun addTask(userId: Int, task: Task) {
        val tasks = tasksMap[userId]

        if (tasks != null) {
            tasks[taskIdCounter] = task.copy(id = taskIdCounter)
        } else {
            tasksMap[userId] = mutableMapOf(Pair(taskIdCounter, task.copy(id = taskIdCounter)))
        }

        taskIdCounter++
    }

    fun deleteTask(userId: Int, taskId: Int) {
        val tasks = tasksMap.getValue(userId)
        tasks.remove(taskId)
    }

    fun updateTask(userId: Int, taskId: Int, newTask: Task) {
        val tasks = tasksMap.getValue(userId)
        tasks[taskId] = newTask.copy(id = taskId)
    }


    val notesMap = mutableMapOf<Int, MutableMap<Int, Note>>()
    private var noteIdCounter = 0

    fun getNotes(userId: Int): List<Note> {
        val notes = notesMap.getOrDefault(userId, mutableMapOf())

        return notes.values.toList()
    }

    fun addNote(userId: Int, note: Note) {
        val notes = notesMap[userId]

        if (notes != null) {
            notes[noteIdCounter] = note.copy(id = noteIdCounter)
        } else {
            notesMap[userId] = mutableMapOf(Pair(noteIdCounter, note.copy(id = noteIdCounter)))
        }

        noteIdCounter++
    }

    fun deleteNote(userId: Int, noteId: Int) {
        val notes = notesMap.getValue(userId)
        notes.remove(noteId)
    }

    fun updateNote(userId: Int, noteId: Int, newNote: Note) {
        val notes = notesMap.getValue(userId)
        notes[noteId] = newNote.copy(id = noteId)
    }

    val tagsMap = mutableMapOf<Int, MutableMap<Int, Tag>>()
    private var tagIdCounter = 0

    fun getTags(userId: Int): List<Tag> {
        val tags = tagsMap.getOrDefault(userId, mutableMapOf())

        return tags.values.toList()
    }

    fun addTag(userId: Int, tag: Tag) {
        val tags = tagsMap[userId]

        if (tags != null) {
            tags[tagIdCounter] = tag.copy(id = tagIdCounter)
        } else {
            tagsMap[userId] = mutableMapOf(Pair(tagIdCounter, tag.copy(id = tagIdCounter)))
        }

        tagIdCounter++
    }

    fun deleteTag(userId: Int, tagId: Int) {
        val tags = tagsMap.getValue(userId)
        tags.remove(tagId)
    }

    fun updateTag(userId: Int, tagId: Int, newTag: Tag) {
        val tags = tagsMap.getValue(userId)
        tags[tagId] = newTag.copy(id = tagId)
    }


}