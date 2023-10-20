package ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import api.ApiClient
import kotlinx.coroutines.launch
import org.opus.models.Tag
import org.opus.models.Task

@Composable
fun taskList(
    tasks: List<Task>,
    setTasks: (List<Task>) -> Unit,
    tags: List<Tag>,
    showAddTask: Boolean = true,
    currentTag: Tag?
) {
    Column {
        if (showAddTask) {
            task(null, setTasks, null, tags, currentTag, tasks)
        }
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth().fillMaxHeight().SimpleVerticalScrollbar(listState),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(tasks.size) {
                task(tasks[it], setTasks, tasks[it].dueDate, tags, currentTag, tasks)
                println(tasks[it].dueDate)
            }
        }
    }
}