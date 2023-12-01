package com.server.opus.plugins.handlers

import com.server.opus.TaskAPI
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.models.opus.dao.DatabaseFactory
import org.models.opus.dao.dao
import org.models.opus.db.TaskEntity
import org.models.opus.db.Tasks

fun Routing.handleUsers() {
    post("/users/{user_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            val user = dao.user(userId)

            if(user == null){
                call.respond(dao.addNewUser(userId))
            } else {
                call.respond(user)
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

            val accessToken = call.request.headers["Authorization"]?.drop(7)

            if(accessToken == null){
                call.response.status(HttpStatusCode.BadRequest)
                return@get
            }

            val gTasks = TaskAPI(accessToken).allTasks()
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
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
}