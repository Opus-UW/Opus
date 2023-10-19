package api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.opus.models.Task

class ApiClient {

    companion object{
        private var instance: ApiClient? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ApiClient().also { instance = it }
        }
    }

    private val baseUrl = "http://0.0.0.0:8080"

    private val httpClient = HttpClient {
        install(ContentNegotiation){
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

        return httpClient.post(url.build()){
            contentType(ContentType.Application.Json)
            setBody(task)
        }.body()
    }

    suspend fun deleteTask(userId: Int, taskId: String): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks/$taskId")
        }
        return httpClient.delete(url.build()).body()
    }

    suspend fun editTask(userId: Int, taskId: String, newTask: Task): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks/$taskId")
        }
        return httpClient.put(url.build()){
            contentType(ContentType.Application.Json)
            setBody(newTask)
        }.body()
    }
}