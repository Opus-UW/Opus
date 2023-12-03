package com.server.opus

import com.server.opus.plugins.configureRouting
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.models.opus.dao.DatabaseFactory
import org.models.opus.dao.dao
import org.models.opus.models.Colour
import org.models.opus.models.DBCredentials
import org.models.opus.models.Tag
import org.models.opus.models.User
import kotlin.test.Test
import kotlin.test.assertEquals

class TagsTest{
    @Test
    fun `returns tags for specific user`() = testApplication {
        lateinit var user: User
        val tags = mutableListOf<Tag>()

        application {
            DatabaseFactory.init()
            DatabaseFactory.setupMockDatabase()
            runBlocking {
                user = dao.addNewUser("0", DBCredentials(0, "", ""))
                val secondUser = dao.addNewUser("1", DBCredentials(0, "", ""))
                tags += dao.addNewTag("a", Colour.CORAL, user.id)
                tags += dao.addNewTag("b", Colour.BLOSSOM, user.id)
                tags += dao.addNewTag("c", Colour.DUSK, user.id)
                dao.addNewTag("d", Colour.PEACH, secondUser.id)
            }
            install(ContentNegotiation) {
                json()
            }
            configureRouting()
        }
        client.get("/users/0/tags").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(tags, Json.decodeFromString<List<Tag>>(body()))
        }
    }

    @Test
    fun `returns empty list if user not found`() = testApplication {
        val tags = mutableListOf<Tag>()

        application {
            DatabaseFactory.init()
            DatabaseFactory.setupMockDatabase()
            install(ContentNegotiation) {
                json()
            }
            configureRouting()
        }
        client.get("/users/0/tags").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(tags, Json.decodeFromString<List<Tag>>(body()))
        }
    }
}