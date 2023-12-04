package com.server.opus.plugins.handlers

import com.server.opus.TaskAPI
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.models.opus.dao.DatabaseFactory
import org.models.opus.dao.dao
import org.models.opus.db.TaskEntity
import org.models.opus.db.Tasks
import org.models.opus.models.DBCredentials

fun Routing.handleUsers() {
    post("/users/{user_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            val user = dao.user(userId)
            val creds = call.receive<DBCredentials>()

            if(user == null){
                call.respond(dao.addNewUser(userId, creds))
            } else {
                call.respond(user)
                dao.editUser(user.id, creds)
            }
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    get("/users/{user_id}/login") {
        try {
            val userId = call.parameters.getOrFail("user_id")

            val user = dao.user(userId)!!
            val gTasks = TaskAPI(user).allTasks()
            for(task in gTasks.items){
                if (DatabaseFactory.dbQuery { TaskEntity.find { Tasks.gTaskId eq task.id }.count().toInt() > 0 }) {
                    if (task.deleted == true) {
                        dao.deleteGTask(task.id)
                    } else {
                        dao.editGTask(task.id, task.status == "completed", task.title, task.due)
                    }
                } else if (task.deleted != true){
                    dao.addNewGTask(task.status == "completed", task.title, task.due, task.id, userId)
                }
            }
            call.response.status(HttpStatusCode.OK)
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
}