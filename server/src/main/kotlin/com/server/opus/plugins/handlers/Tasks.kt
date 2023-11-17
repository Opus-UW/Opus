package com.server.opus.plugins.handlers

import com.server.opus.TaskAPI
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.models.opus.dao.dao
import org.models.opus.models.Task

fun Routing.handleTasks() {
    get("/users/{user_id}/tasks") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            call.respond(dao.userTasks(userId))
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    get("/users/{user_id}/uncompleted-tasks") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            call.respond(dao.userTasks(userId).filter { !it.completed })
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    get("/users/{user_id}/completed-todos") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            call.respond(dao.userTasks(userId).filter { it.completed })
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    post("/users/{user_id}/tasks") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            val task = call.receive<Task>()
            val accessToken = call.response.headers["gtoken"]
            var gTaskId: String? = null

            if(task.dueDate != null && accessToken != null){
                // Create google task
                val gTask = com.google.api.services.tasks.model.Task()
                gTask.apply {
                    setTitle(task.action)
                    setUpdated(task.creationDate.toString())
                    task.dueDate?.let{setDue(it.toString())}
                }
                val createdGTask = TaskAPI(accessToken).createTask(gTask)
                gTaskId = createdGTask.id
            }

            dao.addNewTask(
                task.completed, task.action, task.creationDate.toString(), task.dueDate?.toString(), task.tags, gTaskId, userId
            )

            call.respond(dao.userTasks(userId))
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    delete("/users/{user_id}/tasks/{task_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            val taskId = call.parameters.getOrFail("task_id").toInt()
            val taskGId = dao.taskGId(taskId)
            val accessToken = call.response.headers["gtoken"]

            if(taskGId != null && accessToken !== null){
                // Delete google task
                TaskAPI(accessToken).deleteTask(taskGId)
            }

            dao.deleteTask(taskId)

            call.respond(dao.userTasks(userId))

        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
    put("/users/{user_id}/tasks/{task_id}") {
        try {
            val userId = call.parameters.getOrFail("user_id")
            val taskId = call.parameters.getOrFail("task_id").toInt()
            val accessToken = call.response.headers["gtoken"]

            val task = call.receive<Task>()
            var gTaskId: String? = null

            if(task.dueDate != null && accessToken != null){
                // Modify google task
                val gTask = com.google.api.services.tasks.model.Task()
                gTask.apply {
                    setTitle(task.action)
                    task.dueDate?.let{setDue(it.toString())}
                    val time = Clock.System.now()
                    val timeString = time.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
                    setUpdated(timeString)
                    if (task.completed) setCompleted(timeString)
                }
                val createdGTask = TaskAPI(accessToken).patchTask(gTask)
                gTaskId = createdGTask.id
            }

            dao.editTask(
                taskId, task.completed, task.action, task.creationDate.toString(), task.dueDate?.toString(), task.tags, gTaskId
            )

            call.respond(dao.userTasks(userId))
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
}