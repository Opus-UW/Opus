package com.server.opus.plugins.handlers

import com.google.api.client.util.DateTime
import com.server.opus.TaskAPI
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
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
            val accessToken = call.request.headers["Authorization"]?.drop(7)

            val createdTask = dao.addNewTask(
                task.completed,
                task.action,
                task.creationDate.toString(),
                task.dueDate?.toString(),
                task.tags,
                task.notificationSent,
                task.important,
                null,
                userId
            )

            call.respond(dao.userTasks(userId))

            if (task.dueDate != null && accessToken != null) {
                // Create google task
                val gTask = com.google.api.services.tasks.model.Task()
                gTask.apply {
                    setTitle(task.action)
                    task.dueDate?.let {
                        setDue(DateTime(it.toString()).toStringRfc3339())
                    } ?: setDue(null)
                    if (task.completed) {
                        setHidden(true)
                        setStatus("completed")
                    } else {
                        setHidden(false)
                        setStatus("needsAction")
                    }
                }
                val user = dao.user(userId)!!

                val createdGTask = TaskAPI(user.credentials).createTask(gTask)

                dao.editTask(
                    createdTask.id,
                    createdTask.completed,
                    createdTask.action,
                    createdTask.creationDate.toString(),
                    createdTask.dueDate?.toString(),
                    createdTask.tags,
                    createdTask.important,
                    createdGTask.id
                )
            }
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
            val accessToken = call.request.headers["Authorization"]?.drop(7)

            dao.deleteTask(taskId)

            call.respond(dao.userTasks(userId))

            if (taskGId != null && accessToken !== null) {
                // Delete google task
                val user = dao.user(userId)
                TaskAPI(user!!.credentials).deleteTask(taskGId)
            }
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
            val accessToken = call.request.headers["Authorization"]?.drop(7)

            val task = call.receive<Task>()
            var gTaskId: String? = dao.taskGId(taskId)

            dao.editTask(
                taskId,
                task.completed,
                task.action,
                task.creationDate.toString(),
                task.dueDate?.toString(),
                task.tags,
                task.important,
                gTaskId
            )

            call.respond(dao.userTasks(userId))

            if (task.dueDate != null && accessToken != null) {
                // Modify google task
                val gTask = com.google.api.services.tasks.model.Task()
                gTask.apply {
                    setId(gTaskId)
                    setTitle(task.action)
                    task.dueDate?.let {
                        setDue(DateTime(it.toString()).toStringRfc3339())
                    } ?: setDue(null)

                    if (task.completed) {
                        setHidden(true)
                        setStatus("completed")
                    } else {
                        setHidden(false)
                        setStatus("needsAction")
                    }
                }
                if (gTaskId == null) {
                    val user = dao.user(userId)!!
                    val createdGTask = TaskAPI(user.credentials).createTask(gTask)
                    gTaskId = createdGTask.id
                } else {
                    val user = dao.user(userId)!!
                    TaskAPI(user.credentials).patchTask(gTask)
                }

                dao.editTask(
                    taskId,
                    task.completed,
                    task.action,
                    task.creationDate.toString(),
                    task.dueDate?.toString(),
                    task.tags,
                    task.important,
                    gTaskId
                )
            }


        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            call.response.status(HttpStatusCode.BadRequest)
        }
    }
}