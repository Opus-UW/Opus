package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import utils.minusMonth
import utils.plusMonth
import viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpusTopAppBar(
    viewModel: MainViewModel,
    navigator: Navigator,
    toggleDrawer: () -> Unit
) {
    val currentTag by viewModel.currentTag.collectAsStateWithLifecycle()
    val curDate by viewModel.curDate.collectAsStateWithLifecycle()

    var taskState by remember { mutableStateOf(true) }
    var noteState by remember { mutableStateOf(false) }
    var calendarState by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(currentTag?.title ?: "All")

                if (calendarState) {
                    Row(
                        Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.setCurDate(curDate.minusMonth(1)) },
                            enabled = true
                        ) { Icon(Icons.Default.ArrowBack, contentDescription = "Calender Right") }
                        Text(curDate.month.name)
                        IconButton(
                            onClick = { viewModel.setCurDate(curDate.plusMonth(1)) },
                            enabled = true
                        ) { Icon(Icons.Default.ArrowForward, contentDescription = "Calender Left") }
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                IconButton(
                    onClick = {
                        if (!taskState) {
                            taskState = true
                            noteState = false
                            calendarState = false
                            viewModel.setCurrentScreen("/tasks")
                            navigator.navigate("/tasks")
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.ViewList,
                        tint = if (taskState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Navigate to tasks"
                    )
                }
                IconButton(
                    onClick = {
                        if (!noteState) {
                            taskState = false
                            noteState = true
                            calendarState = false
                            viewModel.setCurrentScreen("/notes")
                            navigator.navigate("/notes")
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.StickyNote2,
                        tint = if (noteState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Navigate to notes"
                    )
                }
                IconButton(
                    onClick = {
                        if (!calendarState) {
                            taskState = false
                            noteState = false
                            calendarState = true
                            viewModel.setCurrentScreen("/calendar")
                            navigator.navigate("/calendar")
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        tint = if (calendarState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Navigate to calendar"
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = toggleDrawer) {
                Icon(Icons.Default.Menu, contentDescription = "Menu Button")
            }
        },

        )


}