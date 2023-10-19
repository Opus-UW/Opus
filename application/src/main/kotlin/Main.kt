import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.material.Card
import api.ApiClient
import ui.components.task
import kotlinx.coroutines.launch
import org.opus.models.Task
import ui.EditScreen
import ui.NavigationBar
import ui.components.taskList

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Opus") {
        val coroutineScope = rememberCoroutineScope()
        val (tasks,setTasks) = remember { mutableStateOf(listOf<Task>()) }
        val getOnClick: () -> Unit = {
            coroutineScope.launch {
                setTasks(ApiClient.getInstance().getTasks(0))
            }
        }
        getOnClick()

        NavigationBar(tasks, setTasks)
    }
}
