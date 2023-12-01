package com.server.opus.plugins

import com.server.opus.GmailAPI
import com.server.opus.TaskAPI
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.datetime.toJavaLocalDateTime
import org.models.opus.dao.DatabaseFactory.dbQuery
import org.models.opus.dao.dao
import org.models.opus.db.TaskEntity
import org.models.opus.db.Tasks
import org.models.opus.models.UserWSData
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*


fun Application.configureSockets() {
    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/chat") {
            println("Adding user!")
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = converter!!.deserialize<UserWSData>(frame, Charset.defaultCharset())
                    if(receivedText.accessToken.isEmpty() || receivedText.userId.isEmpty()) continue
                    val user = dao.user(receivedText.userId)
                    val updatedTasks = TaskAPI(user!!.credentials).modifiedTasks()
                    //println(updatedTasks)
                    updatedTasks.items.forEach { task ->
                        if (dbQuery { TaskEntity.find { Tasks.gTaskId eq task.id }.count().toInt() > 0 }) {
                            if (task.deleted == true) {
                                dao.deleteGTask(task.id)
                            } else {
                                dao.editGTask(task.id, task.status == "completed", task.title, task.due)
                            }
                        } else if (task.deleted != true){
                            dao.addNewGTask(task.status == "completed", task.title, task.due, task.id, receivedText.userId)
                        }
                    }
                    if (updatedTasks.items.isNotEmpty()) {
                        thisConnection.session.send("829 modified")
                    }
                    val allTasks = dao.unsentTasks()
                    for (task in allTasks) {
                        if (task.dueDate == null) {
                            continue
                        }
                        val curDT = LocalDateTime.now()
                        val targetDT = task.dueDate!!.toJavaLocalDateTime()
                        val dur = curDT.until(targetDT, ChronoUnit.DAYS)
                        if (dur <= 1 && dur >= 0 && task.completed == false) {
                            dao.notifyTask(task.id)
                            GmailAPI(user!!.credentials).sendEmail("Task \"${task.action}\" due","Your task \"${task.action}\" is due in < 1 day.\n Good Luck!")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }
    }
}