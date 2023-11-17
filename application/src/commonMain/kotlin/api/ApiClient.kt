package api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.models.opus.models.Note
import org.models.opus.models.Tag
import org.models.opus.models.Task
import org.models.opus.models.User

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

    private var accessToken: String = ""
    fun setAccessToken(value: String) {
        accessToken = value
    }

    private var userId: String = ""
    fun setUserId(value: String) {
        userId = value
    }


    suspend fun getTasks(): List<Task> {
        try {
            val url = URLBuilder().apply {
                takeFrom("$baseUrl/users/$userId/tasks")
            }

            return httpClient.get(url.build()){
                headers{
                    append("gtoken", accessToken)
                }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            System.out.println(e.message)
        }
        return listOf()
    }

    suspend fun getCompletedTasks(): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/completed-tasks")
        }

        return httpClient.get(url.build()){
            headers{
                append("gtoken", accessToken)
            }
        }.body()
    }

    suspend fun getUncompletedTasks(): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/uncompleted-tasks")
        }

        return httpClient.get(url.build()){
            headers{
                append("gtoken", accessToken)
            }
        }.body()
    }

    suspend fun postTask(task: Task): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks")
        }

        return httpClient.post(url.build()) {
            headers{
                append("gtoken", accessToken)
            }
            contentType(ContentType.Application.Json)
            setBody(task)
        }.body()
    }

    suspend fun deleteTask(taskId: Int): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks/$taskId")
        }
        return httpClient.delete(url.build()){
            headers{
                append("gtoken", accessToken)
            }
        }.body()
    }

    suspend fun editTask(taskId: Int, newTask: Task): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks/$taskId")
        }
        return httpClient.put(url.build()) {
            headers{
                append("gtoken", accessToken)
            }
            contentType(ContentType.Application.Json)
            setBody(newTask)
        }.body()
    }

    suspend fun getNotes(): List<Note> {
        try {
            val url = URLBuilder().apply {
                takeFrom("$baseUrl/users/$userId/notes")
            }

            return httpClient.get(url.build()){
                headers{
                    append("gtoken", accessToken)
                }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            System.out.println(e.message)
        }
        return listOf()
    }

    suspend fun postNote(note: Note): List<Note> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/notes")
        }

        return httpClient.post(url.build()) {
            headers{
                append("gtoken", accessToken)
            }
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body()
    }

    suspend fun deleteNote(noteId: Int): List<Note> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/notes/$noteId")
        }
        return httpClient.delete(url.build()){
            headers{
                append("gtoken", accessToken)
            }
        }.body()
    }

    suspend fun editNote(noteId: Int, newNote: Note): List<Note> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/notes/$noteId")
        }
        return httpClient.put(url.build()) {
            headers{
                append("gtoken", accessToken)
            }
            contentType(ContentType.Application.Json)
            setBody(newNote)
        }.body()
    }

    suspend fun getTags(): List<Tag> {
        try {
            val url = URLBuilder().apply {
                takeFrom("$baseUrl/users/$userId/tags")
            }

            return httpClient.get(url.build()) {
                headers{
                    append("gtoken", accessToken)
                }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            System.out.println(e.message)
        }
        return listOf()
    }

    suspend fun postTag(tag: Tag): List<Tag> {
        val url = URLBuilder().apply {
            headers{
                append("gtoken", accessToken)
            }
            takeFrom("$baseUrl/users/$userId/tags")
        }

        return httpClient.post(url.build()) {
            headers{
                append("gtoken", accessToken)
            }
            contentType(ContentType.Application.Json)
            setBody(tag)
        }.body()
    }

    suspend fun deleteTag(tagId: Int): List<Tag> {
        val url = URLBuilder().apply {
            headers{
                append("gtoken", accessToken)
            }
            takeFrom("$baseUrl/users/$userId/tags/$tagId")
        }
        return httpClient.delete(url.build()).body()
    }

    suspend fun editTag(tagId: Int, newTag: Tag): List<Tag> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tags/$tagId")
        }
        return httpClient.put(url.build()) {
            headers{
                append("gtoken", accessToken)
            }
            contentType(ContentType.Application.Json)
            setBody(newTag)
        }.body()
    }


    suspend fun getOrCreateUser(): User {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId")
        }
        return httpClient.post(url.build()) {
            headers{
                append("gtoken", accessToken)
            }
            contentType(ContentType.Application.Json)
        }.body()
    }

}