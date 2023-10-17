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
import components.task
import kotlinx.coroutines.launch
import org.opus.models.Task

@Composable
fun menu() {
    Column() {

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun note() {
    Card(
        onClick = { /* Do something */ },
        modifier = Modifier.size(width = 180.dp, height = 100.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Text("Clickable", Modifier.align(Alignment.Center))
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Opus") {

        val coroutinescope = rememberCoroutineScope()
        val (tasks,setTasks) = remember { mutableStateOf(listOf<Task>()) }
        val getOnClick: () -> Unit = {
            coroutinescope.launch {
                setTasks(ApiClient.getInstance().getTasks(0))
            }
        }

        Column {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                task(null, setTasks)
                tasks.forEach {
                    task(it, setTasks)
                }
            }
            //note()
        }
    }
}
