package api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.opus.models.Note
import org.opus.models.Tag
import org.opus.models.Task

class ApiClient {

    companion object {
        private var instance: ApiClient? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ApiClient().also { instance = it }
        }
    }

    private val baseUrl = "http://0.0.0.0:8080"

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    suspend fun getTasks(userId: Int): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks")
        }

        return httpClient.get(url.build()).body()
    }

    suspend fun getCompletedTasks(userId: Int): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/completed-tasks")
        }

        return httpClient.get(url.build()).body()
    }

    suspend fun getUncompletedTasks(userId: Int): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/uncompleted-tasks")
        }

        return httpClient.get(url.build()).body()
    }

    suspend fun postTask(userId: Int, task: Task): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks")
        }

        return httpClient.post(url.build()) {
            contentType(ContentType.Application.Json)
            setBody(task)
        }.body()
    }

    suspend fun deleteTask(userId: Int, taskId: Int): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks/$taskId")
        }
        return httpClient.delete(url.build()).body()
    }

    suspend fun editTask(userId: Int, taskId: Int, newTask: Task): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks/$taskId")
        }
        return httpClient.put(url.build()) {
            contentType(ContentType.Application.Json)
            setBody(newTask)
        }.body()
    }

    suspend fun getNotes(userId: Int): List<Note> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/notes")
        }

        return httpClient.get(url.build()).body()
    }

    suspend fun postNote(userId: Int, note: Note): List<Note> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/notes")
        }

        return httpClient.post(url.build()) {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body()
    }

    suspend fun deleteNote(userId: Int, noteId: Int): List<Note> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/notes/$noteId")
        }
        return httpClient.delete(url.build()).body()
    }

    suspend fun editNote(userId: Int, noteId: Int, newNote: Note): List<Note> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/notes/$noteId")
        }
        return httpClient.put(url.build()) {
            contentType(ContentType.Application.Json)
            setBody(newNote)
        }.body()
    }

    suspend fun getTags(userId: Int): List<Tag> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tags")
        }

        return httpClient.get(url.build()).body()
    }

    suspend fun postTag(userId: Int, tag: Tag): List<Tag> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tags")
        }

        return httpClient.post(url.build()) {
            contentType(ContentType.Application.Json)
            setBody(tag)
        }.body()
    }

    suspend fun deleteTag(userId: Int, tagId: Int): List<Tag> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tags/$tagId")
        }
        return httpClient.delete(url.build()).body()
    }

    suspend fun editTag(userId: Int, tagId: Int, newTag: Tag): List<Tag> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tags/$tagId")
        }
        return httpClient.put(url.build()) {
            contentType(ContentType.Application.Json)
            setBody(newTag)
        }.body()
    }

}