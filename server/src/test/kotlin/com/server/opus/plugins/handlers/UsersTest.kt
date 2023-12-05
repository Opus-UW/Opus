package com.server.opus.plugins.handlers

import com.server.opus.util.TaskParams
import com.server.opus.util.mockGoogle
import com.server.opus.util.setup
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.models.opus.dao.dao
import org.models.opus.models.DBCredentials
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(Enclosed::class)
class UsersTest {

    class Get {
        @Test
        fun `fetches unsynced tasks from google`() = testApplication {
            mockGoogle(
                tasksOutput = listOf(
                    TaskParams(
                        id = "a", title = "a", due = 0, hidden = false, status = "needsAction"
                    ), TaskParams(
                        id = "b", title = "b", due = 0, hidden = true, status = "needsAction"
                    ), TaskParams(
                        id = "c", title = "c", due = 0, hidden = true, status = "needsAction"
                    ), TaskParams(
                        id = "d", title = "d", due = 0, hidden = false, status = "needsAction"
                    )
                )
            )
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            application {
                setup()
                runBlocking {
                    dao.addNewUser("0", DBCredentials(0, "a", "a"))
                }
            }

            client.get("/users/0/login").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(dao.userTasks("0").size, 4)
        }
    }

    class Post {
        @Test
        fun `returns user in db if exists`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            application {
                setup()
                runBlocking {
                    dao.addNewUser("0", DBCredentials(0, "", ""))
                    assertNotNull(dao.user("0"))
                }
            }

            client.post("/users/0") {
                contentType(ContentType.Application.Json)
                setBody(DBCredentials(0, "a", "a"))
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }

            assertNotNull(dao.user("0"))
        }

        @Test
        fun `creates user in db if does not exist`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            application {
                setup()
                runBlocking {
                    assertNull(dao.user("0"))
                }
            }

            client.post("/users/0") {
                contentType(ContentType.Application.Json)
                setBody(DBCredentials(0, "a", "a"))
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }

            assertNotNull(dao.user("0"))
        }
    }
}