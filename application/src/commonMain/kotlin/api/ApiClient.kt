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

    private val baseUrl = "http://35.239.87.183:8080"

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


    suspend fun getTasks(userId: Int): List<Task> {
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

    suspend fun getCompletedTasks(userId: Int): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/completed-tasks")
        }

        return httpClient.get(url.build()){
            headers{
                append("gtoken", accessToken)
            }
        }.body()
    }

    suspend fun getUncompletedTasks(userId: Int): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/uncompleted-tasks")
        }

        return httpClient.get(url.build()){
            headers{
                append("gtoken", accessToken)
            }
        }.body()
    }

    suspend fun postTask(userId: Int, task: Task): List<Task> {
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

    suspend fun deleteTask(userId: Int, taskId: String): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks/$taskId")
        }
        return httpClient.delete(url.build()){
            headers{
                append("gtoken", accessToken)
            }
        }.body()
    }

    suspend fun editTask(userId: Int, taskId: String, newTask: Task): List<Task> {
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

    suspend fun getNotes(userId: Int): List<Note> {
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

    suspend fun postNote(userId: Int, note: Note): List<Note> {
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

    suspend fun deleteNote(userId: Int, noteId: Int): List<Note> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/notes/$noteId")
        }
        return httpClient.delete(url.build()){
            headers{
                append("gtoken", accessToken)
            }
        }.body()
    }

    suspend fun editNote(userId: Int, noteId: Int, newNote: Note): List<Note> {
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

    suspend fun getTags(userId: Int): List<Tag> {
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

    suspend fun postTag(userId: Int, tag: Tag): List<Tag> {
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

    suspend fun deleteTag(userId: Int, tagId: Int): List<Tag> {
        val url = URLBuilder().apply {
            headers{
                append("gtoken", accessToken)
            }
            takeFrom("$baseUrl/users/$userId/tags/$tagId")
        }
        return httpClient.delete(url.build()).body()
    }

    suspend fun editTag(userId: Int, tagId: Int, newTag: Tag): List<Tag> {
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


    suspend fun getOrCreateUser(userId: String): List<User> {
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