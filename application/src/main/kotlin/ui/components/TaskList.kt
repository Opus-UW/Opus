package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.ApiClient
import kotlinx.coroutines.launch
import org.opus.models.Tag
import org.opus.models.Task

@Composable
fun taskList(
    tasks: List<Task>,
    setTasks: (List<Task>) -> Unit,
    tags: List<Tag>
) {
    Column(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        task(null, setTasks, tags)
        tasks.forEach {
            task(it, setTasks, tags)
        }
    }
}