package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*
import org.models.opus.models.Tag
import org.models.opus.models.Task
import utils.plus
import viewmodels.MainViewModel
import java.time.Instant
import java.time.ZoneId


@Composable
fun optionsTray(
    viewModel: MainViewModel,
    task: Task?,
    isTaskFocused: Boolean,
    isNewTask: Boolean,
    tagStatus: SnapshotStateMap<Tag, Boolean>,
    updateDueDate: (LocalDateTime?) -> Unit
) {
    val (showDueDatePicker, setShowDueDatePickerPicker) = remember { mutableStateOf(false) }
    val (showTags, setShowTags) = remember { mutableStateOf(false) }

    var rootPos by remember { mutableStateOf(Offset.Zero) }
    Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.onSecondary)) {
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
                TextButton(onClick = { setShowTags(true) },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        rootPos = coordinates.positionInRoot()
                    }) {
                    Icon(Icons.Default.Sell, contentDescription = "Tags")
                    ChooseTagMenu(viewModel, showTags, setShowTags, tagStatus)
                }
                Spacer(modifier = Modifier.weight(1f))
                if (!isNewTask)
                    TextButton(onClick = { viewModel.deleteTask(task) },
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            rootPos = coordinates.positionInRoot()
                        }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.tertiary)
                    }
            }
        }
    }

}

@Composable
fun chooseDate(
    showDueDatePicker: Boolean,
    setShowDueDatePickerPicker: (Boolean) -> Unit,
    pos: Offset,
    updateDueDate: (LocalDateTime?) -> Unit
) {
    var selectedDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val (showCalendar, setShowCalendar) = remember { mutableStateOf(false) }
    datePickerDialog(showCalendar, setShowCalendar, updateDueDate)

    DropdownMenu(
        expanded = showDueDatePicker, onDismissRequest = { setShowDueDatePickerPicker(false) }) {
        DropdownMenuItem(onClick = {
            updateDueDate(selectedDate)
            println(selectedDate.date)
            setShowDueDatePickerPicker(false)
        },
            text = {
                Text("Today")
            }
        )

        DropdownMenuItem(onClick = {
            selectedDate += 1
            updateDueDate(selectedDate)
            println(selectedDate.date)
            setShowDueDatePickerPicker(false)
        }, text = {
            Text("Tomorrow")
        }
        )
        DropdownMenuItem(onClick = {
            selectedDate += 7
            updateDueDate(selectedDate)
            println(selectedDate.date)
            setShowDueDatePickerPicker(false)
        },
            text = {
                Text("Next Week")
            }
        )
        DropdownMenuItem(onClick = {
            setShowDueDatePickerPicker(false)
            setShowCalendar(true)
        },
            text = {
                Text("Choose Date")
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePickerDialog(
    showCalendar: Boolean,
    setShowCalendar: (Boolean) -> Unit,
    updateDueDate: (LocalDateTime?) -> Unit
) {
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