package com.server.opus

import com.server.opus.util.DefaultNote
import com.server.opus.util.setup
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.models.opus.dao.dao
import org.models.opus.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Enclosed::class)
class NotesTest {
    class Get {
        @Test
        fun `returns notes for specific user`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val notes = mutableListOf<Note>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    notes += dao.addNewNote("a", "bodya", listOf(), false, user.id)
                    notes += dao.addNewNote("b", "bodyb", listOf(), false, user.id)
                    notes += dao.addNewNote("c", "bodyc", listOf(), false, user.id)
                    dao.addNewNote("d", "bodyd", listOf(), false, secondUser.id)
                }
            }

            client.get("/users/0/notes").apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals(notes, body<List<Note>>())
            }
        }

        @Test
        fun `returns empty list if user not found`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            val notes = mutableListOf<Note>()

            application {
                setup()
            }
            client.get("/users/0/notes").apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals(notes, body<List<Note>>())
            }
        }
    }

    class Post {
        @Test
        fun `adds a note for an existing user`() = testApplication {
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
            client.post("/users/0/notes") {
                contentType(ContentType.Application.Json)
                setBody(DefaultNote)
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }

            assertEquals(dao.userNotes(user.id).size, 1)
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
            client.post("/users/0/notes") {
                contentType(ContentType.Application.Json)
                setBody(DefaultNote)
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, status)
            }

            assertEquals(dao.userNotes("0").size, 0)
        }
    }

    class Put {
        @Test
        fun `modifies a note for specific user`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val notes = mutableListOf<Note>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    notes += dao.addNewNote("a", "bodya", listOf(), false, user.id)
                    notes += dao.addNewNote("b", "bodyb", listOf(), false, user.id)
                    notes += dao.addNewNote("c", "bodyc", listOf(), false, user.id)
                    dao.addNewNote("d", "bodyd", listOf(), false, secondUser.id)
                }
            }

            client.put("/users/0/notes/1") {
                contentType(ContentType.Application.Json)
                setBody(DefaultNote)
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(DefaultNote.copy(id = notes[0].id), dao.note(notes[0].id))


        }

        @Test
        fun `does nothing if note id does not exist`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val notes = mutableListOf<Note>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    notes += dao.addNewNote("a", "bodya", listOf(), false, user.id)
                    notes += dao.addNewNote("b", "bodyb", listOf(), false, user.id)
                    notes += dao.addNewNote("c", "bodyc", listOf(), false, user.id)
                    dao.addNewNote("d", "bodyd", listOf(), false, secondUser.id)
                }
            }

            client.put("/users/0/notes/5") {
                contentType(ContentType.Application.Json)
                setBody(DefaultNote)
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(notes, dao.userNotes(user.id))
        }
    }

    class Delete {
        @Test
        fun `deletes note from user`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val notes = mutableListOf<Note>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    notes += dao.addNewNote("a", "bodya", listOf(), false, user.id)
                    notes += dao.addNewNote("b", "bodyb", listOf(), false, user.id)
                    notes += dao.addNewNote("c", "bodyc", listOf(), false, user.id)
                    dao.addNewNote("d", "bodyd", listOf(), false, secondUser.id)
                }
            }

            client.delete("/users/0/notes/1").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(notes.slice(1..<notes.size), dao.userNotes(user.id))
        }

        @Test
        fun `does nothing if note id does not exist`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val notes = mutableListOf<Note>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    notes += dao.addNewNote("a", "bodya", listOf(), false, user.id)
                    notes += dao.addNewNote("b", "bodyb", listOf(), false, user.id)
                    notes += dao.addNewNote("c", "bodyc", listOf(), false, user.id)
                    dao.addNewNote("d", "bodyd", listOf(), false, secondUser.id)
                }
            }

            client.delete("/users/0/notes/5").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(notes, dao.userNotes(user.id))
        }
    }
}