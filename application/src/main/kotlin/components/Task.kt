package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import api.ApiClient
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.opus.models.Task

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun task(task: Task?, updateTasks: (List<Task>) -> Unit) {
    // Indicates if it's a new task creation bar or not
    val new = task == null
    // Focus variable for task
    var isTaskFocused by remember(task) { mutableStateOf(false) }
    // Column for task + options bar
    Column(
        modifier = Modifier
            .onFocusChanged {
                isTaskFocused = it.isFocused
            }
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Task Action + Input
        val textFieldFocusRequester = remember { FocusRequester() }
        var text by remember(task) { mutableStateOf(task?.action ?: "") }
        Box(modifier = Modifier
            .height(IntrinsicSize.Min)
            .background(Color.White)
            .fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Checkbox / plus sign
                var hovered by remember { mutableStateOf(false) }
                val coroutineScope = rememberCoroutineScope()
                IconButton(onClick = {
                    // delete the task (note change to finished)
                    if (task != null) {
                        coroutineScope.launch {
                            updateTasks(ApiClient.getInstance().deleteTask(0, task.id))
                        }
                    }
                    // For new task
                    // Clicking on the plus focuses text box
                    else{
                        textFieldFocusRequester.requestFocus()
                    }
                }) {
                    Icon(
                        // If create new task, plus and circle icon
                        if (new)
                            if (isTaskFocused) Icons.Outlined.Circle else Icons.Default.Add
                        // For already created tasks, click to check off
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
                                val taskToSend = Task(false, text, LocalDateTime(2002, 12, 21, 0, 0))
                                coroutineScope.launch {
                                    updateTasks(ApiClient.getInstance().postTask(0, taskToSend))
                                }
                                text = ""
                            }
                            true
                        }
                        .focusRequester(textFieldFocusRequester)
                        .width(IntrinsicSize.Max)
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
        chooseOccurrence(showOccurrence, setShowOccurrence, rootPos)
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
fun chooseOccurrence(showOccurrence: Boolean, setShowOccurrence: (Boolean) -> Unit, pos: Offset) {
    var multi by remember { mutableStateOf("1") }
    DropdownMenu(expanded = showOccurrence, onDismissRequest = { setShowOccurrence(false) }) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("Every")
            BasicTextField(value = multi, onValueChange = { multi = it }, Modifier.width(10.dp))
            Text("Weeks")
        }
    }
}
