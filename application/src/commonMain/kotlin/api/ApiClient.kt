package api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.models.opus.models.*

class ApiClient {

    companion object {
        private var instance: ApiClient? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ApiClient().also { instance = it }
        }
    }

    private val baseUrl = "http://0.0.0.0:8080"//"http://35.239.87.183:8080"

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    suspend fun startClientConn(cb: () -> Unit) {
        //runBlocking {
            httpClient.webSocket(method = HttpMethod.Get, host = "0.0.0.0", port = 8080, path = "/chat") {
                val messageOutputRoutine = launch { outputMessages(cb) }
                val userInputRoutine = launch { inputMessages() }

                userInputRoutine.join() // Wait for completion; either "exit" or error
                messageOutputRoutine.cancelAndJoin()
            }
        //}
        httpClient.close()
        println("Connection closed. Goodbye!")
    }

    suspend fun DefaultClientWebSocketSession.outputMessages(cb: () -> Unit) {
        try {
            for (message in incoming) {
                message as? Frame.Text ?: continue
                if (message.readText() == "829 modified") {
                    cb()
                }
                println(message.readText())
            }
        } catch (e: Exception) {
            println("Error while receiving: " + e.localizedMessage)
        }
    }

    suspend fun DefaultClientWebSocketSession.inputMessages() {
        var counter = 0;
        while (true) {
            Thread.sleep(3000L)
            val message = (counter++).toString()
            if (message.equals("exit", true)) return
            try {
                sendSerialized(UserWSData(userId, accessToken))
            } catch (e: Exception) {
                println("Error while sending: " + e.localizedMessage)
                return
            }
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

            return httpClient.get(url.build()) {
                bearerAuth(accessToken)
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

        return httpClient.get(url.build()) {
            bearerAuth(accessToken)
        }.body()
    }

    suspend fun getUncompletedTasks(): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/uncompleted-tasks")
        }

        return httpClient.get(url.build()) {
            bearerAuth(accessToken)
        }.body()
    }

    suspend fun postTask(task: Task): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks")
        }


        return httpClient.post(url.build()) {
            bearerAuth(accessToken)
            contentType(ContentType.Application.Json)
            setBody(task)
        }.body()
    }

    suspend fun deleteTask(taskId: Int): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks/$taskId")
        }
        return httpClient.delete(url.build()) {
            bearerAuth(accessToken)
        }.body()
    }

    suspend fun editTask(taskId: Int, newTask: Task): List<Task> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tasks/$taskId")
        }
        return httpClient.put(url.build()) {
            bearerAuth(accessToken)
            contentType(ContentType.Application.Json)
            setBody(newTask)
        }.body()
    }

    suspend fun getNotes(): List<Note> {
        try {
            val url = URLBuilder().apply {
                takeFrom("$baseUrl/users/$userId/notes")
            }

            return httpClient.get(url.build()) {
                bearerAuth(accessToken)
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.message)
        }
        return listOf()
    }

    suspend fun postNote(note: Note): List<Note> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/notes")
        }

        return httpClient.post(url.build()) {
            bearerAuth(accessToken)
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body()
    }

    suspend fun deleteNote(noteId: Int): List<Note> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/notes/$noteId")
        }
        return httpClient.delete(url.build()) {
            bearerAuth(accessToken)
        }.body()
    }

    suspend fun editNote(noteId: Int, newNote: Note): List<Note> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/notes/$noteId")
        }
        return httpClient.put(url.build()) {
            bearerAuth(accessToken)
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
                bearerAuth(accessToken)
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.message)
        }
        return listOf()
    }

    suspend fun postTag(tag: Tag): List<Tag> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tags")
        }

        return httpClient.post(url.build()) {
            bearerAuth(accessToken)
            contentType(ContentType.Application.Json)
            setBody(tag)
        }.body()
    }

    suspend fun deleteTag(tagId: Int): List<Tag> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tags/$tagId")
        }
        return httpClient.delete(url.build()) {
            bearerAuth(accessToken)
        }.body()
    }

    suspend fun editTag(tagId: Int, newTag: Tag): List<Tag> {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId/tags/$tagId")
        }
        return httpClient.put(url.build()) {
            bearerAuth(accessToken)
            contentType(ContentType.Application.Json)
            setBody(newTag)
        }.body()
    }


    suspend fun getOrCreateUser(): User {
        val url = URLBuilder().apply {
            takeFrom("$baseUrl/users/$userId")
        }
        return httpClient.post(url.build()) {
            bearerAuth(accessToken)
            contentType(ContentType.Application.Json)
        }.body()
    }

}