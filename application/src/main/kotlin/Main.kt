import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import api.ApiClient
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.opus.models.Todo


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun task(task: Todo?, updateTasks: (List<Todo>) -> Unit) {
    var isTaskFocused by remember { mutableStateOf(false) }
    val new = task == null
    var text by remember(task) { mutableStateOf(task?.action ?: "") }

    Column(
        modifier = Modifier
            .onFocusChanged {
                isTaskFocused = it.isFocused
            }.shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp)
            ).width(IntrinsicSize.Min).height(IntrinsicSize.Min)
    ) {
        // Task Action + Input
        Box(modifier = Modifier.height(IntrinsicSize.Min).background(Color.White)) {
            // Checkbox
            Row(verticalAlignment = Alignment.CenterVertically) {
                var hovered by remember { mutableStateOf(false) }
                val coroutinescope = rememberCoroutineScope()
                IconButton(onClick = {
                    if (task != null) {
                        coroutinescope.launch {
                            updateTasks(ApiClient.getInstance().deleteTodo(0, task.id))
                        }
                    }
                }) {
                    Icon(
                        if (new)
                            if (isTaskFocused) Icons.Outlined.Circle else Icons.Default.Add
                        else
                            if (hovered) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                        contentDescription = "Check mark",
                        modifier = Modifier
                            .onPointerEvent(PointerEventType.Enter) {
                                hovered = true
                            }
                            .onPointerEvent(PointerEventType.Exit) {
                                hovered = false
                            }
                    )
                }
                // Text box
                TextField(
                    value = text,
                    placeholder = { Text("Add a task") },
                    onValueChange = { text = it },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Gray,
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key != Key.Enter) return@onKeyEvent false
                            if (keyEvent.type == KeyEventType.KeyUp) {
                                val taskToSend = Todo(false, text, LocalDateTime(2002, 12, 21, 0, 0))
                                coroutinescope.launch {
                                    updateTasks(ApiClient.getInstance().postTodo(0, taskToSend))
                                }
                                text = ""
                            }
                            true
                        }
                )
                // tag colors
                // change to rounded box? or not full width?
                Box(Modifier.align(Alignment.Bottom).fillMaxHeight().width(4.dp).background(Color.Blue))
            }
        }
        // Edit Options Tray
        optionsTray(isTaskFocused)
    }

}

@Composable
fun optionsTray(isTaskFocused: Boolean) {
    val (showCalendar, setShowCalendar) = remember { mutableStateOf(false) }
    val (showOccurrence, setShowOccurrence) = remember { mutableStateOf(false) }

    var rootPos by remember { mutableStateOf(Offset.Zero) }
    Box(modifier = Modifier.fillMaxWidth().background(Color.Gray)) {
        if (isTaskFocused) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { setShowCalendar(true) },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        rootPos = coordinates.positionInRoot()
                    }) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar")
                }
                TextButton(onClick = { setShowOccurrence(true) },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        rootPos = coordinates.positionInRoot()
                    }) {
                    Icon(Icons.Default.Repeat, contentDescription = "Occurring")
                }
                TextButton(onClick = {}) {
                    Icon(Icons.Default.Sell, contentDescription = "Tag")
                }
            }
        }
    }

    if (showCalendar) {
        chooseDate(showCalendar, setShowCalendar, rootPos)
    }

    if (showOccurrence) {
        chooseOccurance(showOccurrence, setShowOccurrence, rootPos)
    }
}

@Composable
fun chooseDate(showCalendar: Boolean, setShowCalendar: (Boolean) -> Unit, pos: Offset) {
    DropdownMenu(expanded = showCalendar, onDismissRequest = { setShowCalendar(false) }) {
        DropdownMenuItem(onClick = {}) {
            Text("Today")
        }
        DropdownMenuItem(onClick = {}) {
            Text("Tomorrow")
        }
        DropdownMenuItem(onClick = {}) {
            Text("Next Week")
        }
        DropdownMenuItem(onClick = {}) {
            Text("Choose Date")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun chooseOccurance(showOccurrence: Boolean, setShowOccurrence: (Boolean) -> Unit, pos: Offset) {
    var multi by remember { mutableStateOf("1") }
    DropdownMenu(expanded = showOccurrence, onDismissRequest = { setShowOccurrence(false) }) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("Every")
            BasicTextField(value = multi, onValueChange = { multi = it }, Modifier.width(10.dp))
            Text("Weeks")
        }
    }
}

@Composable
fun menu() {
    Column() {

    }
}

@Composable
fun note() {

}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Opus") {

        val coroutinescope = rememberCoroutineScope()
        val (tasks,setTaskss) = remember { mutableStateOf(listOf<Todo>()) }
        val setTasks: (List<Todo>) -> Unit = {
            setTaskss(it)
            println(it)
        }
        val getOnClick: () -> Unit = {
            coroutinescope.launch {
                setTasks(ApiClient.getInstance().getTodos(0))
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
        }
    }
}
