package org.models.opus.dao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.models.opus.db.*
import org.models.opus.models.DBCredentials

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.sqlite.JDBC"
        val jdbcURL = "jdbc:sqlite:opusdata.db"
        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            SchemaUtils.create(Users, Notes, Tags, Tasks, NoteTags, TaskTags)
        }
    }

    fun resetDatabase(){
        runBlocking {
            dbQuery {
                SchemaUtils.drop(Users,Notes,Tags,Tasks,NoteTags,TaskTags)
                SchemaUtils.create(Users, Notes, Tags, Tasks, NoteTags, TaskTags)
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}