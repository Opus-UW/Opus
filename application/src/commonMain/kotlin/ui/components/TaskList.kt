package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.models.opus.models.Task
import viewmodels.MainViewModel

@Composable
fun taskList(
    viewModel: MainViewModel,
    tasks: List<Task>,
    showAddTask: Boolean = true,
    defaultDueDate: kotlinx.datetime.LocalDateTime?,
) {
    Column {
        if (showAddTask) {
            task(viewModel, null, null, defaultDueDate)
        }
        Spacer(modifier = Modifier.size(10.dp))
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth().fillMaxHeight().SimpleVerticalScrollbar(listState),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(tasks.size) {
                task(viewModel, tasks[it], tasks[it].dueDate, defaultDueDate)
            }
        }
    }
}