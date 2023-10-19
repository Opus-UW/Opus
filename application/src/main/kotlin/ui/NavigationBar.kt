package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.ApiClient
import kotlinx.coroutines.launch
import org.opus.models.Task

@Composable
fun NavigationBar(tasks: List<Task>, setTasks: (List<Task>) -> Unit) {
    val (showMenu, setShowMenu) = remember { mutableStateOf(false) }
    val toggleMenu: () -> Unit = { setShowMenu(!showMenu) }
    var screen by remember { mutableStateOf("All") }

    val taskMap by remember(tasks) { mutableStateOf(mutableMapOf<String, MutableList<Task>>())}
    val tags by remember(tasks) { mutableStateOf(mutableListOf<String>()) }

    tags.add("All")
    taskMap["All"] = mutableListOf<Task>()

    tasks.forEach{ task ->
        task.tags.forEach { tag ->
            taskMap[tag]?.add(task) ?: { taskMap[tag] = mutableListOf(task); tags.add(tag) }
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
                        taskMap.keys.forEach{ tag ->
                            TextButton(onClick = { screen = tag }) {
                                Text(tag)
                            }
                        }
                    }
                }
            }
            tags.forEach{ tag ->
                if (screen == tag){
                    taskMap[tag]?.let { it1 -> EditScreen(tag, it1, setTasks, toggleMenu, tags) }
                }
            }
// Insert Calendar code here (remember to pass toggleMenu to be able to toggle the menu lol)
//        else if (screen == "Calendar"){
//
//        }
        }
    }
}