package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import ui.components.SimpleVerticalScrollbar
import ui.components.task
import viewmodels.MainViewModel

@Composable
fun TaskScreen(
    viewModel: MainViewModel
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val currentTag by viewModel.currentTag.collectAsStateWithLifecycle()
    val defaultDueDate = null
    val (showCompleted, setShowCompleted) = remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val tagTasks = tasks.filter { if (currentTag != null) it.tags.contains(currentTag) else true }
    val uncompletedTasks = tagTasks.filter { !it.completed }
    val completedTasks = tagTasks.filter { it.completed }

    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        task(viewModel, null, null, null)
        Spacer(modifier = Modifier.size(5.dp))
        LazyColumn(
            state = listState,
            modifier = Modifier.SimpleVerticalScrollbar(listState).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = uncompletedTasks,
                key = { task ->
                    task.id
                }
            ) { task ->
                task(viewModel, task, task.dueDate, defaultDueDate)
            }
            item{
                ToggleSortDivider(showCompleted, setShowCompleted, "Completed", completedTasks.size)
            }
            if (showCompleted) {
                items(
                    items = completedTasks,
                    key = { task ->
                        task.id
                    }
                ) { task ->
                    task(viewModel, task, task.dueDate, defaultDueDate)
                }
            }
        }

    }
}

@Composable
fun ToggleSortDivider(
    show: Boolean,
    setShow: (Boolean) -> Unit,
    label: String,
    amount: Int? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { setShow(!show) }) {
            if (show) {
                Icon(Icons.Default.ExpandMore, contentDescription = "Show Completed Tasks")
            } else {
                Icon(Icons.Default.ChevronRight, contentDescription = "Show Completed Tasks")
            }
        }
        Text(label, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(10.dp))
        if (amount != null) {
            Text(amount.toString())
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}