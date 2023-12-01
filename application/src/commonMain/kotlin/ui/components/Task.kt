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
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.*
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Task
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

    println("DERECK3: $taskDueDate $initialDueDate $defaultDueDate ${task?.dueDate}")


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
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxWidth()
                .border(
                    1.dp,
                    if (isTaskFocused) MaterialTheme.colorScheme.outline else Color.Transparent,
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
                modifier = Modifier.fillMaxWidth().padding(5.dp)
            ) {
                // Checkbox / plus sign
                TaskCheckbox(viewModel, task, new, isTaskFocused, textFieldFocusRequester)

                // Text box
                Row (modifier = Modifier.weight(1f)){
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
                            textStyle = LocalTextStyle.current.merge(TextStyle(color = LocalContentColor.current, textDecoration = if (task != null && task.completed) TextDecoration.LineThrough else TextDecoration.None)),
                            modifier = Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.key != Key.Enter) return@onKeyEvent false
                                    if (keyEvent.type == KeyEventType.KeyUp) {
                                        println (text)
                                        viewModel.updateTask(
                                            text = text,
                                            task = task,
                                            dueDate = taskDueDate,
                                            tagStatus = tagStatus
                                        )
                                        text = ""
                                        focusManager.clearFocus()
                                    }
                                    true
                                }
                                .focusRequester(textFieldFocusRequester).fillMaxWidth(),
                            singleLine = true
                        ){ innerTextField ->
                            TextFieldDefaults.DecorationBox(
                                value = text,
                                interactionSource = interactionSource,
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
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
                        if (dueDate != null) {
                            Row (modifier = Modifier.fillMaxWidth().noRippleClickable(onClick = {textFieldFocusRequester.requestFocus()})){
                                Text("Due on ${dueDate.date}", fontSize = 10.sp)
                            }
                        }
                    }
                }
                if (!new && isTaskFocused && !edit) {
                    IconButton(
                        onClick = { textFieldFocusRequester.requestFocus(); edit = true; },
                    ) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
                } else if (!new && isTaskFocused) {
                    IconButton(
                        onClick = {
                            viewModel.updateTask(task = task, text = text, tagStatus = tagStatus, dueDate = taskDueDate)
                        },
                    ) { Icon(Icons.Default.Done, contentDescription = "Done") }
                }
                // tag colors
                // change to rounded box? or not full width?
                //Box(Modifier.fillMaxHeight().width(4.dp).background(Color.Blue))
            }
        }
        // Edit Options Tray
        if (new || edit) {
            optionsTray(viewModel, task, isTaskFocused, new, tagStatus) { d -> updateDueDate(d) }
        }
    }
}
