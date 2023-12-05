package com.server.opus.plugins.handlers

import com.server.opus.TaskAPI
import com.server.opus.util.DefaultTask
import com.server.opus.util.mockGoogle
import com.server.opus.util.setup
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.models.opus.dao.dao
import org.models.opus.models.DBCredentials
import org.models.opus.models.Task
import org.models.opus.models.User
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Enclosed::class)
class TasksTest {
    class Get {
        @Test
        fun `returns tasks for specific user`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tasks = mutableListOf<Task>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tasks += dao.addNewTask(
                        false, "a", LocalDateTime(1, 1, 1, 1, 1).toString(), null, listOf(), false, false, null, user.id
                    )
                    tasks += dao.addNewTask(
                        true, "a", LocalDateTime(1, 1, 1, 1, 1).toString(), null, listOf(), false, false, null, user.id
                    )
                    tasks += dao.addNewTask(
                        false, "a", LocalDateTime(1, 1, 1, 1, 1).toString(), null, listOf(), false, false, null, user.id
                    )
                    dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        secondUser.id
                    )
                }
            }

            client.get("/users/0/tasks").apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals(tasks, body<List<Task>>())
            }
        }

        @Test
        fun `returns empty list if user not found`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            val tasks = mutableListOf<Task>()

            application {
                setup()
            }
            client.get("/users/0/tasks").apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals(tasks, body<List<Task>>())
            }
        }

        @Test
        fun `returns uncompleted tasks for specific user`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tasks = mutableListOf<Task>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tasks += dao.addNewTask(
                        false, "a", LocalDateTime(1, 1, 1, 1, 1).toString(), null, listOf(), false, false, null, user.id
                    )
                    tasks += dao.addNewTask(
                        true, "a", LocalDateTime(1, 1, 1, 1, 1).toString(), null, listOf(), false, false, null, user.id
                    )
                    tasks += dao.addNewTask(
                        false, "a", LocalDateTime(1, 1, 1, 1, 1).toString(), null, listOf(), false, false, null, user.id
                    )
                    dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        secondUser.id
                    )
                }
            }

            client.get("/users/0/uncompleted-tasks").apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals(tasks.filter { !it.completed }, body<List<Task>>())
            }
        }

        @Test
        fun `returns completed tasks for specific user`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tasks = mutableListOf<Task>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tasks += dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        user.id
                    )
                    tasks += dao.addNewTask(
                        true,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        user.id
                    )
                    tasks += dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        user.id
                    )
                    dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        secondUser.id
                    )
                }
            }

            client.get("/users/0/completed-todos").apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals(tasks.filter { it.completed }, body<List<Task>>())
            }
        }
    }

    class Post {
        @Test
        fun `adds a task for an existing user`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                }
            }
            client.post("/users/0/tasks") {
                contentType(ContentType.Application.Json)
                setBody(DefaultTask)
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }

            assertEquals(dao.userTasks(user.id).size, 1)
        }

        @Test
        fun `returns bad request code if user not found`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            application {
                setup()
            }
            client.post("/users/0/tasks") {
                contentType(ContentType.Application.Json)
                setBody(DefaultTask)
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, status)
            }

            assertEquals(dao.userTasks("0").size, 0)
        }

        @Test
        fun `creates google task if due date is available`() = testApplication {
            mockGoogle()
            mockkConstructor(TaskAPI::class)
            every { anyConstructed<TaskAPI>().createTask(any()) } returns com.google.api.services.tasks.model.Task()

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                }
            }
            client.post("/users/0/tasks") {
                contentType(ContentType.Application.Json)
                setBody(DefaultTask.copy(dueDate = LocalDateTime(1, 1, 1, 1, 1, 1)))
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }

            verify { anyConstructed<TaskAPI>().createTask(any()) }

            assertEquals(dao.userTasks(user.id).size, 1)
        }
    }

    class Put {
        @Test
        fun `modifies a task for specific user`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tasks = mutableListOf<Task>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tasks += dao.addNewTask(
                        false, "a", LocalDateTime(1, 1, 1, 1, 1).toString(), null, listOf(), false, false, null, user.id
                    )
                    tasks += dao.addNewTask(
                        true, "a", LocalDateTime(1, 1, 1, 1, 1).toString(), null, listOf(), false, false, null, user.id
                    )
                    tasks += dao.addNewTask(
                        false, "a", LocalDateTime(1, 1, 1, 1, 1).toString(), null, listOf(), false, false, null, user.id
                    )
                    dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        secondUser.id
                    )
                }
            }

            client.put("/users/0/tasks/1") {
                contentType(ContentType.Application.Json)
                setBody(DefaultTask)
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(DefaultTask.copy(id = tasks[0].id), dao.task(tasks[0].id))


        }

        @Test
        fun `does nothing if task id does not exist`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tasks = mutableListOf<Task>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tasks += dao.addNewTask(
                        false, "a", LocalDateTime(1, 1, 1, 1, 1).toString(), null, listOf(), false, false, null, user.id
                    )
                    tasks += dao.addNewTask(
                        true, "a", LocalDateTime(1, 1, 1, 1, 1).toString(), null, listOf(), false, false, null, user.id
                    )
                    tasks += dao.addNewTask(
                        false, "a", LocalDateTime(1, 1, 1, 1, 1).toString(), null, listOf(), false, false, null, user.id
                    )
                    dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        secondUser.id
                    )
                }
            }

            client.put("/users/0/tasks/5") {
                contentType(ContentType.Application.Json)
                setBody(DefaultTask)
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(tasks, dao.userTasks(user.id))
        }

        @Test
        fun `modifies gtask if gtask id and due date exists`() = testApplication {
            mockGoogle()
            mockkConstructor(TaskAPI::class)
            every { anyConstructed<TaskAPI>().patchTask(any()) } returns com.google.api.services.tasks.model.Task()

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tasks = mutableListOf<Task>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tasks += dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        LocalDateTime(1, 1, 1, 1, 1).toString(),
                        listOf(),
                        false,
                        false,
                        "0",
                        user.id
                    )
                    tasks += dao.addNewTask(
                        true,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        "0",
                        user.id
                    )
                    tasks += dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        "0",
                        user.id
                    )
                    dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        "0",
                        secondUser.id
                    )
                }
            }

            client.put("/users/0/tasks/1") {
                contentType(ContentType.Application.Json)
                setBody(DefaultTask.copy(action = "a", dueDate = LocalDateTime(1, 1, 1, 1, 1, 1)))
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }

            verify { anyConstructed<TaskAPI>().patchTask(any()) }
            assertEquals(tasks, dao.userTasks(user.id))
        }

        @Test
        fun `create gtask if only due date exists`() = testApplication {
            mockGoogle()
            mockkConstructor(TaskAPI::class)
            every { anyConstructed<TaskAPI>().createTask(any()) } returns com.google.api.services.tasks.model.Task()

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tasks = mutableListOf<Task>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tasks += dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        LocalDateTime(1, 1, 1, 1, 1).toString(),
                        listOf(),
                        false,
                        false,
                        null,
                        user.id
                    )
                    tasks += dao.addNewTask(
                        true,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        "0",
                        user.id
                    )
                    tasks += dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        "0",
                        user.id
                    )
                    dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        "0",
                        secondUser.id
                    )
                }
            }

            client.put("/users/0/tasks/1") {
                contentType(ContentType.Application.Json)
                setBody(DefaultTask.copy(action = "a", dueDate = LocalDateTime(1, 1, 1, 1, 1, 1)))
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }

            verify { anyConstructed<TaskAPI>().createTask(any()) }
            assertEquals(tasks, dao.userTasks(user.id))
        }
    }

    class Delete {
        @Test
        fun `deletes task from user`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tasks = mutableListOf<Task>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tasks += dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        user.id
                    )
                    tasks += dao.addNewTask(
                        true,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        user.id
                    )
                    tasks += dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        user.id
                    )
                    dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        secondUser.id
                    )
                }
            }

            client.delete("/users/0/tasks/1").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(tasks.slice(1..<tasks.size), dao.userTasks(user.id))
        }

        @Test
        fun `does nothing if task id does not exist`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tasks = mutableListOf<Task>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tasks += dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        user.id
                    )
                    tasks += dao.addNewTask(
                        true,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        user.id
                    )
                    tasks += dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        user.id
                    )
                    dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        null,
                        secondUser.id
                    )
                }
            }

            client.delete("/users/0/tasks/5").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(tasks, dao.userTasks(user.id))
        }

        @Test
        fun `deletes gtask if gtask id exists`() = testApplication {
            mockGoogle()
            mockkConstructor(TaskAPI::class)
            every { anyConstructed<TaskAPI>().deleteTask(any()) } returns Unit

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tasks = mutableListOf<Task>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tasks += dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        LocalDateTime(1, 1, 1, 1, 1).toString(),
                        listOf(),
                        false,
                        false,
                        "0",
                        user.id
                    )
                    tasks += dao.addNewTask(
                        true,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        "0",
                        user.id
                    )
                    tasks += dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        "0",
                        user.id
                    )
                    dao.addNewTask(
                        false,
                        "a",
                        LocalDateTime(1, 1, 1, 1, 1, 1).toString(),
                        null,
                        listOf(),
                        false,
                        false,
                        "0",
                        secondUser.id
                    )
                }
            }

            client.delete("/users/0/tasks/1").apply {
                assertEquals(HttpStatusCode.OK, status)
            }

            verify { anyConstructed<TaskAPI>().deleteTask(any()) }
            assertEquals(tasks.slice(1..<tasks.size), dao.userTasks(user.id))
        }
    }
}