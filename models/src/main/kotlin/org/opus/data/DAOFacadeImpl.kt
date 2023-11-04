package org.opus.data

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.opus.models.*
import org.opus.data.DatabaseFactory.dbQuery

class DAOFacadeImpl : DAOFacade {
    private fun resultRowToTask(row: ResultRow) = Task(
        id = row[Tasks.id].value,
        completed = row[Tasks.completed],
        action = row[Tasks.action],
        creationDate = LocalDateTime.parse(row[Tasks.creationDate]),
        dueDate =  row[Tasks.dueDate]?.let{
            println("text is: ${it}")
            LocalDateTime.parse(it)
                                          },
        tags = listOf()
    )
    private fun resultRowToNote(row: ResultRow) = Note(
        id = row[Notes.id].value,
        title = row[Notes.title],
        body = row[Notes.body],
        tags = listOf()
    )
    private fun resultRowToTag(row: ResultRow) = Tag(
        id = row[Tags.id].value,
        title = row[Tags.title],
        colour = row[Tags.colour],
    )

    override suspend fun allTasks(): List<Task> = dbQuery{
        Tasks.selectAll().map{
            resultRowToTask(it).copy(tags = TaskTags.select{
                TaskTags.taskId eq it[Tasks.id]
            }.map(::resultRowToTag))
        }
    }

    override suspend fun task(id: Int): Task? = dbQuery{
        Tasks
            .select { Tasks.id eq id }
            .map(::resultRowToTask)
            .singleOrNull()
    }

    override suspend fun userTasks(userId: Int): List<Task> = dbQuery{
        Tasks
            .select { Tasks.userId eq userId }
            .map(::resultRowToTask)
    }

    override suspend fun addNewTask(
        completed: Boolean,
        action: String,
        creationDate: String,
        dueDate: String?,
        userId: Int
    ): Task? = dbQuery{
        val insertStatement = Tasks.insert {
            it[Tasks.completed] = completed
            it[Tasks.action] = action
            it[Tasks.creationDate] = creationDate
            it[Tasks.dueDate] = dueDate
            it[Tasks.userId] = userId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTask)
    }

    override suspend fun editTask(
        id: Int,
        completed: Boolean,
        action: String,
        creationDate: String,
        dueDate: String?
    ): Boolean = dbQuery{
        Tasks.update({ Tasks.id eq id }) {
            it[Tasks.completed] = completed
            it[Tasks.action] = action
            it[Tasks.creationDate] = creationDate
            it[Tasks.dueDate] = dueDate
        } > 0
    }

    override suspend fun deleteTask(id: Int): Boolean = dbQuery{
        Tasks.deleteWhere { Tasks.id eq id } > 0
    }

    override suspend fun allNotes(): List<Note> = dbQuery{
        Notes.selectAll().map{
            resultRowToNote(it).copy(tags = NoteTags.select{
                NoteTags.noteId eq it[Notes.id]
            }.map(::resultRowToTag))
        }
    }

    override suspend fun note(id: Int): Note? = dbQuery{
        Notes
            .select { Notes.id eq id }
            .map(::resultRowToNote)
            .singleOrNull()
    }

    override suspend fun userNotes(userId: Int): List<Note> = dbQuery{
        Notes
            .select { Notes.userId eq userId }
            .map(::resultRowToNote)
    }

    override suspend fun addNewNote(title: String, body: String, userId: Int): Note? = dbQuery{
        val insertStatement = Notes.insert {
            it[Notes.title] = title
            it[Notes.body] = body
            it[Notes.userId] = userId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToNote)
    }

    override suspend fun editNote(id: Int, title: String, body: String): Boolean = dbQuery {
        Notes.update({ Notes.id eq id }) {
            it[Notes.title] = title
            it[Notes.body] = body
        } > 0
    }

    override suspend fun deleteNote(id: Int): Boolean = dbQuery {
        Notes.deleteWhere { Notes.id eq id } > 0
    }

    override suspend fun allTags(): List<Tag> = dbQuery {
        Tags.selectAll().map(::resultRowToTag)
    }

    override suspend fun tag(id: Int): Tag? = dbQuery {
        Tags
            .select { Tags.id eq id }
            .map(::resultRowToTag)
            .singleOrNull()
    }

    override suspend fun userTags(userId: Int): List<Tag> = dbQuery {
        Tags
            .select { Tags.userId eq userId }
            .map(::resultRowToTag)
    }

    override suspend fun addNewTag(title: String, colour: Colour, userId: Int): Tag? = dbQuery {
        val insertStatement = Tags.insert {
            it[Tags.title] = title
            it[Tags.colour] = colour
            it[Tags.userId] = userId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTag)
    }

    override suspend fun editTag(id: Int, title: String, colour: Colour): Boolean = dbQuery {
        Tags.update({ Tags.id eq id }) {
            it[Tags.title] = title
            it[Tags.colour] = colour
        } > 0
    }

    override suspend fun deleteTag(id: Int): Boolean = dbQuery {
        Tags.deleteWhere { Tags.id eq id } > 0
    }

    override suspend fun getTaskTags(id: Int): List<Tag> = dbQuery {
        TaskTags
            .select { TaskTags.taskId eq id }
            .map(::resultRowToTag)
    }

    override suspend fun getNoteTags(id: Int): List<Tag> = dbQuery {
        NoteTags
            .select { NoteTags.noteId eq id }
            .map(::resultRowToTag)
    }

    override suspend fun linkTaskToTag(taskId: Int, tagId: Int): Boolean {
        TaskTags.insert {
            it[TaskTags.tagId] = tagId
            it[TaskTags.taskId] = taskId
        }
        return true
    }

    override suspend fun linkNoteToTag(noteId: Int, tagId: Int): Boolean {
        NoteTags.insert {
            it[NoteTags.tagId] = tagId
            it[NoteTags.noteId] = noteId
        }
        return true
    }

    override suspend fun unlinkTaskToTag(taskId: Int, tagId: Int): Boolean= dbQuery {
        TaskTags.deleteWhere { (TaskTags.tagId eq tagId).and(TaskTags.taskId eq taskId) } > 0
    }

    override suspend fun unlinkNoteToTag(noteId: Int, tagId: Int): Boolean = dbQuery{
        NoteTags.deleteWhere { (NoteTags.tagId eq tagId).and(NoteTags.noteId eq noteId) } > 0
    }
}
val dao: DAOFacade = DAOFacadeImpl()