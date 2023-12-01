package ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.*
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Tag
import org.models.opus.models.Task
import utils.plus
import viewmodels.MainViewModel

// Source: https://proandroiddev.com/remove-ripple-effect-from-clickable-and-toggleable-widget-in-jetpack-compose-16b154265283
inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun task(
    viewModel: MainViewModel,
    task: Task?,
    dueDate: LocalDateTime? = null,
    defaultDueDate: LocalDateTime?,
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
    var editCount by remember(task) { mutableStateOf(0) }
    val darkTheme by viewModel.darkTheme.collectAsStateWithLifecycle()


    // Task variables
    var text by remember(task) { mutableStateOf(task?.action ?: "") }
    val currentTag by viewModel.currentTag.collectAsStateWithLifecycle()
    var oldCurrentTag by remember (task) { mutableStateOf<Tag?>(null) }

    val initialDueDate: LocalDateTime? = defaultDueDate ?: task?.dueDate
    var taskDueDate by remember(task) { mutableStateOf(initialDueDate) }

    fun updateDueDate(dueDate: LocalDateTime? = null) {
        if (dueDate != null) taskDueDate = dueDate
    }

    // Get current tags for the task
    val tagStatus = remember(task) { mutableStateMapOf(
        *tags.map {
            it to (task?.tags?.contains(it) ?: (it == currentTag) || (it == currentTag))
        }.toTypedArray()) }

    // Update tagStatus to see if tags are removed or added
    LaunchedEffect(tags){
        tags.forEach{tag ->
            if (!tagStatus.containsKey(tag)){
                tagStatus[tag] = false
            }
        }

        tagStatus.forEach{
            if (!tags.contains(it.key)){
                tagStatus.remove(it.key)
            }
        }
    }

    // Update currentTag and tagStatus based on current tag
    LaunchedEffect(currentTag){
        if (new){
            if (currentTag != null) {
                tagStatus[currentTag!!] = true
            }
            if (oldCurrentTag != null) {
                tagStatus[oldCurrentTag!!] = false
            }
            oldCurrentTag = currentTag
        }
    }

    when {
        !edit && (editCount > 0)-> {
            viewModel.updateTask(task = task, text = text, tagStatus = tagStatus, dueDate = taskDueDate)
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
                .background(
                    if (isTaskFocused) MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp) else MaterialTheme.colorScheme.surfaceColorAtElevation(
                        1.dp
                    )
                )
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(5.dp)
            ) {
                // Checkbox / plus sign
                TaskCheckbox(viewModel, task, new, isTaskFocused, textFieldFocusRequester)

                // Text box
                Row(modifier = Modifier.weight(1f)) {
                    Column {
                        val interactionSource = remember { MutableInteractionSource() }
                        val visualTransformation = VisualTransformation.None
                        BasicTextField(
                            value = text,
                            onValueChange = { text = it },
                            interactionSource = interactionSource,
                            visualTransformation = visualTransformation,
                            readOnly = !(new || edit),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            textStyle = LocalTextStyle.current.merge(
                                TextStyle(
                                    color = LocalContentColor.current,
                                    textDecoration = if (task != null && task.completed) TextDecoration.LineThrough else TextDecoration.None
                                )
                            ),
                            modifier = Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.key != Key.Enter) return@onKeyEvent false
                                    if (keyEvent.type == KeyEventType.KeyUp) {

                                        viewModel.updateTask(
                                            text = text,
                                            task = task,
                                            dueDate = taskDueDate,
                                            tagStatus = tagStatus
                                        )
                                        if (new) {
                                            taskDueDate = null
                                            text = ""
                                            tagStatus.forEach {
                                                if (it.value) {
                                                    tagStatus[it.key] = false
                                                }
                                            }
                                        }
                                        focusManager.clearFocus()
                                    }
                                    true
                                }
                                .focusRequester(textFieldFocusRequester).fillMaxWidth(),
                            singleLine = true
                        ) { innerTextField ->
                            TextFieldDefaults.DecorationBox(
                                value = text,
                                interactionSource = interactionSource,
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                ),
                                enabled = true,
                                innerTextField = innerTextField,
                                singleLine = true,
                                visualTransformation = visualTransformation,
                                placeholder = { if (new) Text("Add a task") },
                                contentPadding = PaddingValues(0.dp)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                                .noRippleClickable(onClick = { textFieldFocusRequester.requestFocus() })
                        ) {
                            if (taskDueDate != null) {
                                taskDisplayDate(taskDueDate!!)
                            } else if (dueDate != null) {
                                taskDisplayDate(dueDate)
                            }

                            if (tagStatus.containsValue(true) && (taskDueDate != null || dueDate != null)) {
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Divider(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.width(2.dp).height(10.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                            displayTags(darkTheme ?: false, tagStatus)
                        }
                    }
                }
                if (!new && isTaskFocused && !edit) {
                    IconButton(
                        onClick = { textFieldFocusRequester.requestFocus(); edit = true; editCount++},
                    ) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
                } else if (!new && isTaskFocused) {
                    IconButton(
                        onClick = {
                            viewModel.updateTask(task = task, text = text, tagStatus = tagStatus, dueDate = taskDueDate)
                        },
                    ) { Icon(Icons.Default.Done, contentDescription = "Done") }
                }
            }
        }
        // Edit Options Tray
        if (new || edit) {
            optionsTray(viewModel, task, isTaskFocused, new, tagStatus) { d -> updateDueDate(d) }
        }
    }
}


@Composable
fun taskDisplayDate(
    date: LocalDateTime
) {
    val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    Column {
        Spacer(modifier = Modifier.height(6.dp))
        Icon(
            Icons.Default.CalendarToday,
            contentDescription = null,
            modifier = Modifier.size(15.dp)
        )
    }
    Spacer(modifier = Modifier.width(2.dp))
    if (date.date == todayDate.date) {
        Text("Due Today", fontSize = 10.sp)
    } else if (date.date == (todayDate + 1).date) {
        Text("Due Tomorrow", fontSize = 10.sp)
    } else {
        Text("Due on ${date.date}", fontSize = 10.sp)
    }
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}