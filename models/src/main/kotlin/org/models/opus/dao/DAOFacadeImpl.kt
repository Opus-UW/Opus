package org.models.opus.dao

import org.jetbrains.exposed.sql.SizedCollection
import org.models.opus.dao.DatabaseFactory.dbQuery
import org.models.opus.db.*
import org.models.opus.models.*
import org.models.opus.utils.toLDT

class DAOFacadeImpl : DAOFacade {
    private fun entityToTask(entity: TaskEntity) = Task(
        id = entity.id.value,
        completed = entity.completed,
        action = entity.action,
        creationDate = entity.creationDate.toLDT(),
        dueDate = entity.dueDate?.let {
            if (it != "null") return@let it.toLDT()
            null
        },
        important = entity.important,
        tags = entity.tags.map(::entityToTag)
    )

    private fun entityToNote(entity: NoteEntity) = Note(
        id = entity.id.value, title = entity.title, body = entity.body, tags = entity.tags.map(::entityToTag), pinned = entity.pinned
    )

    private fun entityToTag(entity: TagEntity) = Tag(
        id = entity.id.value, title = entity.title, colour = entity.colour.toEnum<Colour>() ?: Colour.CORAL
    )

    private fun entityToUser(entity: UserEntity) = User(
        id = entity.id.value,
        credentials = entity.credentials
    )


    override suspend fun allTasks(): List<Task> = dbQuery {
        TaskEntity.all().map(::entityToTask)
    }

    override suspend fun task(id: Int): Task? = dbQuery {
        TaskEntity.find { Tasks.id eq id }.map(::entityToTask).singleOrNull()
    }

    override suspend fun unsentTasks(): List<Task> = dbQuery {
        TaskEntity.find{ Tasks.notificationSent eq false}.map(::entityToTask)
    }

    override suspend fun taskGId(id: Int): String? = dbQuery {
        TaskEntity.find { Tasks.id eq id }.firstOrNull()?.gTaskId
    }

    override suspend fun userTasks(userId: String): List<Task> = dbQuery {
        TaskEntity.find { Tasks.userId eq userId }.map(::entityToTask)
    }

    override suspend fun addNewTask(
        completed: Boolean, action: String, creationDate: String, dueDate: String?, tags: List<Tag>, notificationSent: Boolean, important: Boolean, gTaskId: String?, userId: String
    ): Task = dbQuery {
        entityToTask(TaskEntity.new {
            this.completed = completed
            this.action = action
            this.creationDate = creationDate
            this.dueDate = dueDate
            this.user = UserEntity.findById(userId) ?: throw Exception()
            this.tags = TagEntity.forIds(tags.map { it.id })
            this.notificationSent = notificationSent
            this.important = important
            this.gTaskId = gTaskId
        })
    }

    override suspend fun addNewGTask(
        completed: Boolean, action: String, dueDate: String, gTaskId: String?, userId: String
    ): Task = dbQuery {
        entityToTask(TaskEntity.new {
            this.completed = completed
            this.action = action
            this.dueDate = dueDate.toLDT().toString()
            this.creationDate = dueDate.toLDT().toString()
            this.user = UserEntity.findById(userId) ?: throw Exception()
            this.notificationSent = false
            this.important = false
            this.gTaskId = gTaskId
            this.tags = SizedCollection()
        })
    }

    override suspend fun editTask(
        id: Int, completed: Boolean, action: String, creationDate: String, dueDate: String?, tags: List<Tag>, notificationSent: Boolean, important: Boolean, gTaskId: String?
    ): Boolean = dbQuery {
        val entity = TaskEntity.find { Tasks.id eq id }.singleOrNull() ?: return@dbQuery false
        entity.apply {
            this.completed = completed
            this.action = action
            this.creationDate = creationDate
            this.dueDate = dueDate
            this.tags = TagEntity.forIds(tags.map { it.id })
            this.notificationSent = notificationSent
            this.important = important
            this.gTaskId = gTaskId
        }

        return@dbQuery true
    }

    override suspend fun editGTask(
        gTaskId: String, completed: Boolean, action: String, dueDate: String?
    ): Boolean = dbQuery {
        val entity = TaskEntity.find { Tasks.gTaskId eq gTaskId }.singleOrNull() ?: return@dbQuery false
        entity.apply {
            this.completed = completed
            this.action = action
            this.dueDate = dueDate?.toLDT()?.toString()
        }

        return@dbQuery true
    }

    override suspend fun deleteTask(id: Int): Boolean = dbQuery {
        val tasks = TaskEntity.find { Tasks.id eq id }
        if (tasks.count().toInt() == 0) return@dbQuery false
        tasks.forEach { it.delete() }
        return@dbQuery true
    }

    override suspend fun deleteGTask(gTaskId: String): Boolean = dbQuery {
        val tasks = TaskEntity.find { Tasks.gTaskId eq gTaskId }
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

    override suspend fun addNewNote(title: String, body: String, tags: List<Tag>, pinned: Boolean, userId: String): Note = dbQuery {
        entityToNote(NoteEntity.new {
            this.title = title
            this.body = body
            this.user = UserEntity.findById(userId) ?: throw Exception()
            this.pinned = pinned
            this.tags = TagEntity.forIds(tags.map { it.id })
        })
    }

    override suspend fun editNote(id: Int, title: String, body: String, tags: List<Tag>, pinned: Boolean): Boolean = dbQuery {
        val entity = NoteEntity.find { Notes.id eq id }.singleOrNull() ?: return@dbQuery false
        entity.apply {
            this.title = title
            this.body = body
            this.tags = TagEntity.forIds(tags.map { it.id })
            this.pinned = pinned
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
            this.colour = colour.ordinal
            this.user = UserEntity.findById(userId) ?: throw Exception()
        })
    }

    override suspend fun editTag(id: Int, title: String, colour: Colour): Boolean = dbQuery {
        val entity = TagEntity.find { Tags.id eq id }.singleOrNull() ?: return@dbQuery false
        entity.apply {
            this.title = title
            this.colour = colour.ordinal
        }
        return@dbQuery true
    }

    override suspend fun deleteTag(id: Int): Boolean = dbQuery {
        val tags = TagEntity.find { Tags.id eq id }
        if (tags.count().toInt() == 0) return@dbQuery false
        tags.forEach { it.delete() }
        return@dbQuery true
    }

    override suspend fun allUsers(): List<User> = dbQuery {
        UserEntity.all().map(::entityToUser)
    }

    override suspend fun user(id: String): User? = dbQuery {
        UserEntity.find { Users.id eq id }.map(::entityToUser).singleOrNull()
    }

    override suspend fun addNewUser(id: String, credentials: DBCredentials): User = dbQuery {
        entityToUser(UserEntity.new(id){
            this.credentials = credentials
        })
    }

    override suspend fun editUser(id: String, credentials: DBCredentials): Boolean = dbQuery {
        val entity = UserEntity.find { Users.id eq id }.singleOrNull() ?: return@dbQuery false
        entity.apply {
            this.credentials = credentials
        } // Does nothing for now, change when user has more data
        return@dbQuery true
    }

    override suspend fun deleteUser(id: String): Boolean = dbQuery {
        val users = UserEntity.find { Users.id eq id }
        if (users.count().toInt() == 0) return@dbQuery false
        users.forEach { it.delete() }
        return@dbQuery true
    }
}

val dao: DAOFacade = DAOFacadeImpl()