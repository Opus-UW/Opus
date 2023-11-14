import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import api.ApiClient
import kotlinx.coroutines.launch
import org.models.opus.models.Colour
import org.models.opus.models.Note
import org.models.opus.models.Tag
import org.models.opus.models.Task
import ui.EditScreen
import ui.NavigationBar
import ui.calendarScreen

@Composable
fun App() {
    val coroutineScope = rememberCoroutineScope()
    val (tasks,setTasks) = remember { mutableStateOf(listOf<Task>()) }
    val (notes, setNotes) = remember { mutableStateOf(listOf<Note>()) }
    val (tags, setTags) = remember { mutableStateOf(listOf<Tag>()) }
    var currentTag by remember{mutableStateOf<Tag?>(null)}
    // Get tasks from server
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            setTasks(ApiClient.getInstance().getTasks(0))
        }

        coroutineScope.launch {
            setTags(ApiClient.getInstance().getTags(0))
        }

        coroutineScope.launch {
            setNotes(ApiClient.getInstance().getNotes(0))
        }
    }
    val (showMenu, setShowMenu) = remember { mutableStateOf(false) }
    val toggleMenu: () -> Unit = { setShowMenu(!showMenu) }
    var (screen, setScreen) = remember { mutableStateOf("All") }

    val taskMap = remember(tasks,notes,tags) { mutableStateMapOf(*tags.map{it to Pair(mutableSetOf<Task>(), mutableSetOf<Note>())}.toTypedArray())}
    val allTag = Tag("All", Colour(255, 255, 255))
    taskMap[allTag] = Pair(tasks.toMutableSet(), notes.toMutableSet())

    tasks.forEach { task ->
        task.tags.forEach { tag ->
            taskMap[tag]?.first?.add(task)
        }
    }

    notes.forEach { note ->
        note.tags.forEach { tag ->
            taskMap[tag]?.second?.add(note)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    setNotes(ApiClient.getInstance().postNote(0, Note("", "", tags=currentTag?.let{listOf(it)}?:listOf())))
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) {
        Row {
            if (showMenu) {
                NavigationBar(tags, setTags, setScreen, toggleMenu)
            }

            if (screen == "All"){
                currentTag = null
                EditScreen("All", tasks, setTasks, showMenu, toggleMenu, tags, setTags, notes, setNotes, null, null)
            } else if (screen == "Calendar") {
                calendarScreen(toggleMenu, showMenu, tasks, setTasks, tags, setTags)
            }

            tags.forEach{ tag ->
                if (screen == tag.title) {
                    currentTag = tag
                    taskMap[tag]?.let { it1 -> EditScreen(tag.title, it1.first.toMutableList(), setTasks, showMenu, toggleMenu, tags, setTags, it1.second.toMutableList(), setNotes, tag, null) }
                }
            }
        }
    }
}
