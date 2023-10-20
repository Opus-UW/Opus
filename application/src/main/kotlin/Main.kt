import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import api.ApiClient
import kotlinx.coroutines.launch
import org.opus.models.Colour
import org.opus.models.Note
import org.opus.models.Tag
import org.opus.models.Task
import ui.EditScreen
import ui.NavigationBar

fun main() = application {
    val coroutineScope = rememberCoroutineScope()
    val (tasks,setTasks) = remember { mutableStateOf(listOf<Task>()) }
    val (notes, setNotes) = remember { mutableStateOf(listOf<Note>()) }
    val (tags, setTags) = remember { mutableStateOf(listOf<Tag>()) }
    // Get tasks from server
    coroutineScope.launch {
        setTasks(ApiClient.getInstance().getTasks(0))
    }

    coroutineScope.launch {
        setTags(ApiClient.getInstance().getTags(0))
    }

    coroutineScope.launch {
        setNotes(ApiClient.getInstance().getNotes(0))
    }

    Window(onCloseRequest = ::exitApplication, title = "Opus") {


        val (showMenu, setShowMenu) = remember { mutableStateOf(false) }
        val toggleMenu: () -> Unit = { setShowMenu(!showMenu) }
        var (screen, setScreen) = remember { mutableStateOf("All") }

        val taskMap by remember(tasks) { mutableStateOf(mutableMapOf<Tag, MutableList<Task>>())}
        val allTag = Tag("All", Colour(255, 255, 255))
        taskMap[allTag] = tasks.toMutableList()

        tasks.forEach{ task ->
            task.tags.forEach { tag ->
                taskMap[tag]?.add(task)
            }
        }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        setNotes(ApiClient.getInstance().postNote(0, Note("", "")))
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note")
                }
            }
        ) {
            Row {
                if (showMenu) {
                    NavigationBar(tags, setTags, setScreen, toggleMenu, taskMap)
                }

                if (screen == "All"){
                    EditScreen("All", tasks, setTasks, showMenu, toggleMenu, tags, notes, setNotes)
                } else if (screen == "Calendar"){
                    // Insert Calendar here
                }

                tags.forEach{ tag ->
                    if (screen == tag.title) {
                        taskMap[tag]?.let { it1 -> EditScreen(tag.title, it1, setTasks, showMenu, toggleMenu, tags, notes, setNotes) }
                    }
                }
            }
        }
    }
}
