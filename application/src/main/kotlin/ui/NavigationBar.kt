package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.opus.models.Colour
import org.opus.models.Tag
import org.opus.models.Task

@Composable
fun NavigationBar(tasks: List<Task>, setTasks: (List<Task>) -> Unit) {
    val (showMenu, setShowMenu) = remember { mutableStateOf(false) }
    val toggleMenu: () -> Unit = { setShowMenu(!showMenu) }
    var screen by remember { mutableStateOf("All") }

    val taskMap by remember(tasks) { mutableStateOf(mutableMapOf<String, MutableList<Task>>()) }
    val tags by remember(tasks) { mutableStateOf(mutableListOf<Tag>()) }

    tags.add(Tag("All", Colour(255, 255, 255)))
    taskMap["All"] = mutableListOf<Task>()

    tasks.forEach { task ->
        task.tags.forEach { tag ->
            taskMap[tag.title]?.add(task) ?: { taskMap[tag.title] = mutableListOf(task); tags.add(tag) }
        }
        taskMap["All"]?.add(task)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        Row {
            if (showMenu) {
                Box(modifier = Modifier.fillMaxHeight().width(IntrinsicSize.Min)) {
                    Column(modifier = Modifier) {
                        IconButton(onClick = { toggleMenu() }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu Button")
                        }
                        TextButton(onClick = { screen = "Calendar" }) {
                            Text("Calendar")
                        }
                        taskMap.keys.forEach { tag ->
                            TextButton(onClick = { screen = tag }) {
                                Text(tag)
                            }
                        }
                    }
                }
            }
            tags.forEach { tag ->
                if (screen == tag.title) {
                    taskMap[tag.title]?.let { it1 -> EditScreen(tag.title, it1, setTasks, showMenu, toggleMenu, tags) }
                }
            }
            if (screen == "Calendar") {
                calendarScreen(toggleMenu, showMenu)
            }
        }
    }
}