package com.server.opus.plugins.handlers

import com.server.opus.util.DefaultTag
import com.server.opus.util.setup
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.models.opus.dao.dao
import org.models.opus.models.Colour
import org.models.opus.models.DBCredentials
import org.models.opus.models.Tag
import org.models.opus.models.User
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Enclosed::class)
class TagsTest {
    class Get {
        @Test
        fun `returns tags for specific user`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tags = mutableListOf<Tag>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tags += dao.addNewTag("a", Colour.CORAL, user.id)
                    tags += dao.addNewTag("b", Colour.BLOSSOM, user.id)
                    tags += dao.addNewTag("c", Colour.DUSK, user.id)
                    dao.addNewTag("d", Colour.PEACH, secondUser.id)
                }
            }

            client.get("/users/0/tags").apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals(tags, body<List<Tag>>())
            }
        }

        @Test
        fun `returns empty list if user not found`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            val tags = mutableListOf<Tag>()

            application {
                setup()
            }
            client.get("/users/0/tags").apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals(tags, body<List<Tag>>())
            }
        }
    }

    class Post {
        @Test
        fun `adds a tag for an existing user`() = testApplication {
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
            client.post("/users/0/tags") {
                contentType(ContentType.Application.Json)
                setBody(DefaultTag)
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }

            assertEquals(dao.userTags(user.id).size, 1)
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
            client.post("/users/0/tags") {
                contentType(ContentType.Application.Json)
                setBody(DefaultTag)
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, status)
            }

            assertEquals(dao.userTags("0").size, 0)
        }
    }

    class Put {
        @Test
        fun `modifies a tag for specific user`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tags = mutableListOf<Tag>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tags += dao.addNewTag("a", Colour.CORAL, user.id)
                    tags += dao.addNewTag("b", Colour.BLOSSOM, user.id)
                    tags += dao.addNewTag("c", Colour.DUSK, user.id)
                    dao.addNewTag("d", Colour.PEACH, secondUser.id)
                }
            }

            client.put("/users/0/tags/1") {
                contentType(ContentType.Application.Json)
                setBody(Tag("aa", Colour.CORAL))
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(tags[0].copy(title = "aa"), dao.tag(tags[0].id))


        }

        @Test
        fun `does nothing if tag id does not exist`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tags = mutableListOf<Tag>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tags += dao.addNewTag("a", Colour.CORAL, user.id)
                    tags += dao.addNewTag("b", Colour.BLOSSOM, user.id)
                    tags += dao.addNewTag("c", Colour.DUSK, user.id)
                    dao.addNewTag("d", Colour.PEACH, secondUser.id)
                }
            }

            client.put("/users/0/tags/5") {
                contentType(ContentType.Application.Json)
                setBody(Tag("aa", Colour.CORAL))
            }.apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(tags, dao.userTags(user.id))
        }
    }

    class Delete {
        @Test
        fun `deletes tag from user`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tags = mutableListOf<Tag>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tags += dao.addNewTag("a", Colour.CORAL, user.id)
                    tags += dao.addNewTag("b", Colour.BLOSSOM, user.id)
                    tags += dao.addNewTag("c", Colour.DUSK, user.id)
                    dao.addNewTag("d", Colour.PEACH, secondUser.id)
                }
            }

            client.delete("/users/0/tags/1").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(tags.slice(1..<tags.size), dao.userTags(user.id))
        }

        @Test
        fun `does nothing if tag id does not exist`() = testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            lateinit var user: User
            val tags = mutableListOf<Tag>()

            application {
                setup()
                runBlocking {
                    user = dao.addNewUser("0", DBCredentials(0, "", ""))
                    val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                    tags += dao.addNewTag("a", Colour.CORAL, user.id)
                    tags += dao.addNewTag("b", Colour.BLOSSOM, user.id)
                    tags += dao.addNewTag("c", Colour.DUSK, user.id)
                    dao.addNewTag("d", Colour.PEACH, secondUser.id)
                }
            }

            client.delete("/users/0/tags/5").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(tags, dao.userTags(user.id))
        }
    }


}