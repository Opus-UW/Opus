package ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Tag
import org.models.opus.models.Task
import viewmodels.MainViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun task(
    viewModel: MainViewModel,
    task: Task?,
    dueDate: LocalDateTime? = null,
    defaultDueDate: kotlinx.datetime.LocalDateTime?,
) {
    // Get data from viewModel
    val tags by viewModel.tags.collectAsStateWithLifecycle()

    // Indicates if it's a new task creation bar or not
    val new = task == null
    // Focus variable for task
    val focusManager = LocalFocusManager.current
    val textFieldFocusRequester = remember(task) { FocusRequester() }
    var isTaskFocused by remember(task) { mutableStateOf(false) }
    var edit by remember(isTaskFocused) { mutableStateOf(false) }

    // Task variables
    var text by remember(task) { mutableStateOf(task?.action ?: "") }
    val currentTag by viewModel.currentTag.collectAsStateWithLifecycle()

    val initialDueDate: LocalDateTime? = defaultDueDate ?: task?.dueDate
    var taskDueDate by remember(task) { mutableStateOf(initialDueDate) }


    fun updateDueDate(dueDate: LocalDateTime? = null) {
        if (dueDate != null) taskDueDate = dueDate
    }

    // Get current tags for the task
    val tagStatus = remember(tags) { mutableStateMapOf(*tags.map { it to false }.toTypedArray()) }
    LaunchedEffect(Unit) {
        task?.tags?.forEach { tag ->
            tagStatus[tag] = true
        }
        if (currentTag != null) {
            tagStatus[currentTag!!] = true
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
                TaskCheckbox(viewModel, task, new, isTaskFocused, textFieldFocusRequester)

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
                                viewModel.updateTask(text = text, task = task)
                                text = ""
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
                            viewModel.updateTask(task = task, tagStatus = tagStatus, dueDate = taskDueDate)
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
            optionsTray(viewModel, task, isTaskFocused, new, tagStatus) { d -> updateDueDate(d) }
        }
    }
}
