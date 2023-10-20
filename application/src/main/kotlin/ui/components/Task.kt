package ui.components

import androidx.compose.foundation.*
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import api.ApiClient
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.opus.models.Tag
import org.opus.models.Task

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun task(task: Task?, updateTasks: (List<Task>) -> Unit, tags: List<Tag>) {
    // Indicates if it's a new task creation bar or not
    val new = task == null
    // Focus variable for task
    val focusManager = LocalFocusManager.current
    val textFieldFocusRequester = remember(task) { FocusRequester() }
    var isTaskFocused by remember(task) { mutableStateOf(false) }
    var edit by remember(isTaskFocused) { mutableStateOf(false) }
    var isCheckboxHovered by remember { mutableStateOf(false) }
    var isTextboxHovered by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Task variables
    var text by remember(task) { mutableStateOf(task?.action ?: "") }
    var taskTags by remember(task) { mutableStateOf(task?.tags ?: listOf<Tag>()) }

    fun updateTask(){
        // Get current time
        val time = Clock.System.now()
        val taskToSend =
            Task(
                false,
                text,
                time.toLocalDateTime(TimeZone.currentSystemDefault()),
                time.toLocalDateTime(TimeZone.currentSystemDefault()),
                taskTags
            )
        if (new) {
            coroutineScope.launch {
                updateTasks(ApiClient.getInstance().postTask(0, taskToSend))
            }
            text = ""
        } else {
            // Insert update code here
            coroutineScope.launch {
                task?.let {
                    updateTasks(
                        ApiClient.getInstance()
                            .editTask(0, it.id, taskToSend.copy(id = it.id))
                    )
                }
            }
        }
    }

    // Column for task + options bar
    Column(
        modifier = Modifier
            .onFocusChanged {
                isTaskFocused = it.hasFocus;
            }
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Task Action + Input
        Box(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .background(Color.White)
                .fillMaxWidth()
                .border(1.dp, if (isTaskFocused) Color.Blue else Color.Transparent)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Checkbox / plus sign
                IconButton(onClick = {
                    // delete the task (note change to finished)
                    if (task != null) {
                        coroutineScope.launch {
                            updateTasks(ApiClient.getInstance().editTask(0, task.id, task.copy(completed = true)))
                        }
                    }
                    // For new task
                    // Clicking on the plus focuses text box
                    else {
                        textFieldFocusRequester.requestFocus()
                    }
                }) {
                    Icon(
                        // If create new task, plus and circle icon
                        if (new) {
                            if (isTaskFocused) Icons.Outlined.Circle else Icons.Default.Add
                        }
                        // For already created tasks, click to check off
                        else {
                            if (isCheckboxHovered) Icons.Default.CheckCircle else Icons.Outlined.Circle
                        },
                        contentDescription = "Check mark",
                        modifier = Modifier
                            .onPointerEvent(PointerEventType.Enter) {
                                isCheckboxHovered = true
                            }
                            .onPointerEvent(PointerEventType.Exit) {
                                isCheckboxHovered = false
                            }
                    )
                }

                // Text box
                TextField(
                    value = text,
                    placeholder = { if (new) Text("Add a task") },
                    onValueChange = { text = it },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        disabledTextColor = Color.Black,
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    readOnly = !(new || edit),
                    modifier = Modifier
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key != Key.Enter) return@onKeyEvent false
                            if (keyEvent.type == KeyEventType.KeyUp) {
                                updateTask()
                                focusManager.clearFocus()
                            }
                            true
                        }
                        .focusRequester(textFieldFocusRequester)
                        .weight(1f)
                        .onPointerEvent(PointerEventType.Enter) {
                            isTextboxHovered = true
                        }
                        .onPointerEvent(PointerEventType.Exit) {
                            isTextboxHovered = false
                        }
                )
                if (!new && isTaskFocused && !edit) {
                    IconButton(
                        onClick = { textFieldFocusRequester.requestFocus(); edit = true; },
                    ) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
                }
                else if (!new && isTaskFocused) {
                    IconButton(
                        onClick = {
                            updateTask()
                        },
                    ) { Icon(Icons.Default.Done, contentDescription = "Done") }
                }
                // tag colors
                // change to rounded box? or not full width?
                Box(Modifier.fillMaxHeight().width(4.dp).background(Color.Blue))
            }
        }
//        print("Edit: ")
//        println(edit)
        // Edit Options Tray
        if (new || edit) {
            optionsTray(isTaskFocused, tags, new)
        }
    }

}

@Composable
fun optionsTray(isTaskFocused: Boolean, tags: List<Tag>, isNewTask: Boolean) {
    val (showCalendar, setShowCalendar) = remember { mutableStateOf(false) }
    val (showOccurrence, setShowOccurrence) = remember { mutableStateOf(false) }
    val (showTags, setShowTags) = remember { mutableStateOf(false) }

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
                TextButton(onClick = { setShowTags(true) },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        rootPos = coordinates.positionInRoot()
                    }) {
                    Icon(Icons.Default.Sell, contentDescription = "Tags")
                }
                if(! isNewTask)
                    TextButton(onClick = { println("me when delet") },
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            rootPos = coordinates.positionInRoot()
                        }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
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

    if (showTags) {
        chooseTags(showTags, setShowTags, rootPos, tags)
    }
}

@Composable
fun chooseDate(showCalendar: Boolean, setShowCalendar: (Boolean) -> Unit, pos: Offset) {
    DropdownMenu(
        expanded = showCalendar, onDismissRequest = { setShowCalendar(false) }) {
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
    DropdownMenu(
        expanded = showOccurrence,
        onDismissRequest = { setShowOccurrence(false) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("Every")
            BasicTextField(value = multi, onValueChange = { multi = it }, Modifier.width(10.dp))
            Text("Weeks")
        }
    }
}

@Composable
fun chooseTags(showTags: Boolean, setShowTags: (Boolean) -> Unit, pos: Offset, tags: List<Tag>) {
    var newTag by remember { mutableStateOf("") }
    DropdownMenu(
        expanded = showTags,
        onDismissRequest = { setShowTags(false) }
    ) {
        Column {
            tags.forEach { tag ->
                Text(tag.title)
            }
            Row {
                Icon(Icons.Default.Add, contentDescription = "Add Tag")
                BasicTextField(value = newTag, onValueChange = { newTag = it }, Modifier.width(10.dp))
            }
        }
    }
}