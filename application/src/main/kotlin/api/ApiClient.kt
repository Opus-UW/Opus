package api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.opus.models.Todo

class ApiClient {

    companion object{
        private var instance: ApiClient? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ApiClient().also { instance = it }
        }
    }

    private val baseUrl = "http://192.168.0.56:8080"

    private val httpClient = HttpClient {
        install(ContentNegotiation){
            json()
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }
    suspend fun getTodos(userId: Int): List<Todo> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/todos") // Replace with your API endpoint
        }

        return httpClient.get(url.build()).body()
    }

    suspend fun getCompletedTodos(userId: Int): List<Todo> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/completed-todos") // Replace with your API endpoint
        }

        return httpClient.get(url.build()).body()
    }

    suspend fun getUncompletedTodos(userId: Int): List<Todo> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/uncompleted-todos") // Replace with your API endpoint
        }

        return httpClient.get(url.build()).body()
    }

    suspend fun postTodo(userId: Int, todo: Todo): List<Todo> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/todos") // Replace with your API endpoint
        }

        return httpClient.post(url.build()){
            contentType(ContentType.Application.Json)
            setBody(todo)
        }.body()
    }

    suspend fun deleteTodo(userId: Int, todoId: String): List<Todo> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/todos/$todoId") // Replace with your API endpoint
        }
        return httpClient.delete(url.build()).body()
    }
}