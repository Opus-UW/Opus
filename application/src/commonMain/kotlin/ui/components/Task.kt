package ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
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
//import androidx.compose.ui.input.pointer.PointerEventType
//import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import api.ApiClient
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.models.opus.models.Tag
import org.models.opus.models.Task
import utils.plus
import java.time.Instant
import java.time.ZoneId


@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun task(
    task: Task?,
    updateTasks: (List<Task>) -> Unit,
    dueDate: LocalDateTime? = null,
    tags: List<Tag>,
    setTags: (List<Tag>) -> Unit,
    currentTag: Tag?,
    tasks: List<Task>,
    defaultDueDate: kotlinx.datetime.LocalDateTime?,
) {
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
    var initialDueDate: LocalDateTime?
    if (defaultDueDate != null) {
        initialDueDate = defaultDueDate
    } else {
        initialDueDate = task?.dueDate
    }
    var taskDueDate by remember(task, tasks) { mutableStateOf(initialDueDate) }

    fun updateDueDate(dueDate: LocalDateTime? = null) {
        println(dueDate?.date)
        if (dueDate != null) taskDueDate = dueDate
    }

    var tagStatus = remember(tags) { mutableStateMapOf(*tags.map { it to false }.toTypedArray()) }
    LaunchedEffect(Unit) {
        task?.tags?.forEach { tag ->
            tagStatus[tag] = true
        }
        if (currentTag != null) {
            tagStatus[currentTag] = true
        }
    }


    fun updateTask() {
        // Get current time
        val time = Clock.System.now()
        val newTaskTags = mutableListOf<Tag>()
        tagStatus.forEach { entry ->
            if (entry.value) {
                newTaskTags.add(entry.key)
            }
        }
        val taskToSend =
            Task(
                false,
                text,
                time.toLocalDateTime(TimeZone.currentSystemDefault()),
                taskDueDate,
                newTaskTags.toList()
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

    fun deleteTask() {
        coroutineScope.launch {
            task?.let { updateTasks(ApiClient.getInstance().deleteTask(0, it.id)) }
        }
    }

    // Column for task + options bar
    Column(
        modifier = Modifier
            .onFocusChanged {
                isTaskFocused = it.hasFocus
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
                .border(
                    1.dp,
                    if (isTaskFocused) Color.Blue else Color.Transparent,
                    shape = if (edit || new) RoundedCornerShape(
                        8.dp,
                        8.dp,
                        0.dp,
                        0.dp
                    ) else RoundedCornerShape(8.dp)
                )
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
                            updateTasks(
                                ApiClient.getInstance().editTask(0, task.id, task.copy(completed = !task.completed))
                            )
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
                        else if (task != null && task.completed) {
                            if (isCheckboxHovered) Icons.Outlined.Circle else Icons.Default.CheckCircle
                        } else {
                            if (isCheckboxHovered) Icons.Default.CheckCircle else Icons.Outlined.Circle
                        },
                        contentDescription = "Check mark",
                        modifier = Modifier
//                            .onPointerEvent(PointerEventType.Enter) {
//                                isCheckboxHovered = true
//                            }
//                            .onPointerEvent(PointerEventType.Exit) {
//                                isCheckboxHovered = false
//                            }
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
                    textStyle = TextStyle(textDecoration = if (task != null && task.completed) TextDecoration.LineThrough else TextDecoration.None),
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
//                        .onPointerEvent(PointerEventType.Enter) {
//                            isTextboxHovered = true
//                        }
//                        .onPointerEvent(PointerEventType.Exit) {
//                            isTextboxHovered = false
//                        }
                )
                if (dueDate != null) {
                    Text("Due on ${dueDate.date}")
                }
                if (!new && isTaskFocused && !edit) {
                    IconButton(
                        onClick = { textFieldFocusRequester.requestFocus(); edit = true; },
                    ) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
                } else if (!new && isTaskFocused) {
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
        // Edit Options Tray
        if (new || edit) {
            optionsTray(isTaskFocused, tags, new, setTags, tagStatus, { deleteTask() }, { d -> updateDueDate(d) })
        }
    }
}

@Composable
fun optionsTray(
    isTaskFocused: Boolean,
    tags: List<Tag>,
    isNewTask: Boolean,
    setTags: (List<Tag>) -> Unit,
    tagStatus: SnapshotStateMap<Tag, Boolean>,
    deleteTask: () -> Unit,
    updateDueDate: (LocalDateTime?) -> Unit
) {
    val (showDueDatePicker, setShowDueDatePickerPicker) = remember { mutableStateOf(false) }
    val (showOccurrence, setShowOccurrence) = remember { mutableStateOf(false) }
    val (showTags, setShowTags) = remember { mutableStateOf(false) }

    var rootPos by remember { mutableStateOf(Offset.Zero) }
    Box(modifier = Modifier.fillMaxWidth().background(Color.Gray)) {
        if (isTaskFocused) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { setShowDueDatePickerPicker(true) },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        rootPos = coordinates.positionInRoot()
                    }) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar")
                    chooseDate(showDueDatePicker, setShowDueDatePickerPicker, rootPos, updateDueDate)

                }
                TextButton(onClick = { setShowOccurrence(true) },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        rootPos = coordinates.positionInRoot()
                    }) {
                    Icon(Icons.Default.Repeat, contentDescription = "Occurring")
                    chooseOccurrence(showOccurrence, setShowOccurrence, rootPos)

                }
                TextButton(onClick = { setShowTags(true) },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        rootPos = coordinates.positionInRoot()
                    }) {
                    Icon(Icons.Default.Sell, contentDescription = "Tags")
                    ChooseTagMenu(showTags, setShowTags, rootPos, tags, setTags, tagStatus)
                }
                Spacer(modifier = Modifier.weight(1f))
                if (!isNewTask)
                    TextButton(onClick = { deleteTask() },
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            rootPos = coordinates.positionInRoot()
                        }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
            }
        }
    }

}

@Composable
fun chooseDate(showDueDatePicker: Boolean, setShowDueDatePickerPicker: (Boolean) -> Unit, pos: Offset, updateDueDate:(LocalDateTime?) -> Unit) {
    var selectedDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val (showCalendar, setShowCalendar) = remember { mutableStateOf(false) }
    datePickerDialog(showCalendar, setShowCalendar, updateDueDate)

    DropdownMenu(
        expanded = showDueDatePicker, onDismissRequest = { setShowDueDatePickerPicker(false) }) {
        DropdownMenuItem(onClick = {
            updateDueDate(selectedDate)
            println(selectedDate.date)
            setShowDueDatePickerPicker(false)} ) {
            Text("Today")
        }
        DropdownMenuItem(onClick = {
            selectedDate += 1
            updateDueDate(selectedDate)
            println(selectedDate.date)
            setShowDueDatePickerPicker(false)
        }) {
            Text("Tomorrow")
        }
        DropdownMenuItem(onClick = {
            selectedDate += 7
            updateDueDate(selectedDate)
            println(selectedDate.date)
            setShowDueDatePickerPicker(false)
        }) {
            Text("Next Week")
        }
        DropdownMenuItem(onClick = {
            setShowDueDatePickerPicker(false)
            setShowCalendar(true)
        }) {
            Text("Choose Date")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePickerDialog(showCalendar: Boolean, setShowCalendar: (Boolean) -> Unit, updateDueDate:(LocalDateTime?) -> Unit) {
    if (showCalendar) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
        DatePickerDialog(
            onDismissRequest = {
                setShowCalendar(false)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        setShowCalendar(false)
                        if (datePickerState.selectedDateMillis != null) {
                            println(datePickerState.selectedDateMillis)
                            val mill: Long = datePickerState.selectedDateMillis!!
                            val chosenDate = Instant.ofEpochMilli(mill).atZone(ZoneId.systemDefault()).toLocalDateTime()
                            val kotlinDate = chosenDate.toKotlinLocalDateTime() + 1
                            updateDueDate(kotlinDate)
                        }
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        setShowCalendar(false)
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
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