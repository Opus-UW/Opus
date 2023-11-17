package org.models.opus.dao

import kotlinx.datetime.LocalDateTime
import org.models.opus.dao.DatabaseFactory.dbQuery
import org.models.opus.db.*
import org.models.opus.models.Colour
import org.models.opus.models.Note
import org.models.opus.models.Tag
import org.models.opus.models.Task

class DAOFacadeImpl : DAOFacade {
    private fun entityToTask(entity: TaskEntity) = Task(
        id = entity.id.value,
        completed = entity.completed,
        action = entity.action,
        creationDate = LocalDateTime.parse(entity.creationDate),
        dueDate = entity.dueDate?.let {
            if (it != "null") return@let LocalDateTime.parse(it)
            null
        },
        tags = entity.tags.map(::entityToTag)
    )

    private fun entityToNote(entity: NoteEntity) = Note(
        id = entity.id.value, title = entity.title, body = entity.body, tags = entity.tags.map(::entityToTag)
    )

    private fun entityToTag(entity: TagEntity) = Tag(
        id = entity.id.value, title = entity.title, colour = entity.colour
    )

    override suspend fun allTasks(): List<Task> = dbQuery {
        TaskEntity.all().map(::entityToTask)
    }

    override suspend fun task(id: String): Task? = dbQuery {
        TaskEntity.find { Tasks.id eq id }.map(::entityToTask).singleOrNull()
    }

    override suspend fun userTasks(userId: String): List<Task> = dbQuery {
        TaskEntity.find { Tasks.userId eq userId }.map(::entityToTask)
    }

    override suspend fun addNewTask(
        completed: Boolean, action: String, creationDate: String, dueDate: String?, tags: List<Tag>, userId: String
    ): Task = dbQuery {
        entityToTask(TaskEntity.new {
            this.completed = completed
            this.action = action
            this.creationDate = creationDate
            this.dueDate = dueDate
            this.user = UserEntity.findById(userId) ?: throw Exception()
            this.tags = TagEntity.forIds(tags.map { it.id })
        })
    }

    override suspend fun editTask(
        id: String, completed: Boolean, action: String, creationDate: String, dueDate: String?, tags: List<Tag>
    ): Boolean = dbQuery {
        val entity = TaskEntity.find { Tasks.id eq id }.singleOrNull() ?: return@dbQuery false
        entity.apply {
            this.completed = completed
            this.action = action
            this.creationDate = creationDate
            this.dueDate = dueDate
            this.tags = TagEntity.forIds(tags.map { it.id })
        }

        return@dbQuery true
    }

    override suspend fun deleteTask(id: String): Boolean = dbQuery {
        val tasks = TaskEntity.find { Tasks.id eq id }
        if (tasks.count().toInt() == 0) return@dbQuery false
        tasks.forEach { it.delete() }
        return@dbQuery true
    }

    override suspend fun allNotes(): List<Note> = dbQuery {
        NoteEntity.all().map(::entityToNote)
    }

    override suspend fun note(id: Int): Note? = dbQuery {
        NoteEntity.find { Notes.id eq id }.map(::entityToNote).singleOrNull()
    }

    override suspend fun userNotes(userId: String): List<Note> = dbQuery {
        NoteEntity.find { Notes.userId eq userId }.map(::entityToNote)
    }

    override suspend fun addNewNote(title: String, body: String, tags: List<Tag>, userId: String): Note = dbQuery {
        entityToNote(NoteEntity.new {
            this.title = title
            this.body = body
            this.user = UserEntity.findById(userId) ?: throw Exception()
            this.tags = TagEntity.forIds(tags.map { it.id })
        })
    }

    override suspend fun editNote(id: Int, title: String, body: String, tags: List<Tag>): Boolean = dbQuery {
        val entity = NoteEntity.find { Notes.id eq id }.singleOrNull() ?: return@dbQuery false
        entity.apply {
            this.title = title
            this.body = body
            this.tags = TagEntity.forIds(tags.map { it.id })
        }

        return@dbQuery true
    }

    override suspend fun deleteNote(id: Int): Boolean = dbQuery {
        val notes = NoteEntity.find { Notes.id eq id }
        if (notes.count().toInt() == 0) return@dbQuery false
        notes.forEach { it.delete() }
        return@dbQuery true
    }

    override suspend fun allTags(): List<Tag> = dbQuery {
        TagEntity.all().map(::entityToTag)
    }

    override suspend fun tag(id: Int): Tag? = dbQuery {
        TagEntity.find { Tags.id eq id }.map(::entityToTag).singleOrNull()
    }

    override suspend fun userTags(userId: String): List<Tag> = dbQuery {
        TagEntity.find { Tags.userId eq userId }.map(::entityToTag)
    }

    override suspend fun addNewTag(title: String, colour: Colour, userId: String): Tag = dbQuery {
        entityToTag(TagEntity.new {
            this.title = title
            this.colour = colour
            this.user = UserEntity.findById(userId) ?: throw Exception()
        })
    }

    override suspend fun editTag(id: Int, title: String, colour: Colour): Boolean = dbQuery {
        val entity = TagEntity.find { Tags.id eq id }.singleOrNull() ?: return@dbQuery false
        entity.apply {
            this.title = title
            this.colour = colour
        }
        return@dbQuery true
    }

    override suspend fun deleteTag(id: Int): Boolean = dbQuery {
        val tags = TagEntity.find { Tags.id eq id }
        if (tags.count().toInt() == 0) return@dbQuery false
        tags.forEach { it.delete() }
        return@dbQuery true
    }
}

val dao: DAOFacade = DAOFacadeImpl()