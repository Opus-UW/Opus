package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.models.opus.models.Tag
import org.models.opus.models.Task

@Composable
fun taskList(
    tasks: List<Task>,
    setTasks: (List<Task>) -> Unit,
    tags: List<Tag>,
    setTags: (List<Tag>) -> Unit,
    showAddTask: Boolean = true,
    currentTag: Tag?
) {
    Column {
        if (showAddTask) {
            task(null, setTasks, null, tags, setTags, currentTag,  tasks)
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
                task(tasks[it], setTasks, tasks[it].dueDate, tags, setTags, currentTag, tasks)
            }
        }
    }
}