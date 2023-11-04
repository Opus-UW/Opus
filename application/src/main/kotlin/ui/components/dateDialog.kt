package ui.components

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.opus.models.Tag
import org.opus.models.Task

@Composable
fun DateDialog(
    date: kotlinx.datetime.LocalDateTime,
    showDateDialog: Boolean,
    setDateDialog: (Boolean) -> Unit,
    tasks: List<Task>,
    setTasks: (List<Task>) -> Unit,
    tags: List<Tag>,
    setTags: (List<Tag>) -> Unit
) {
    if (showDateDialog) {
        println(date)
        println(tasks)
        Dialog(onDismissRequest = { setDateDialog(false) }) {
            Card(
                modifier = Modifier.fillMaxSize().heightIn(0.dp, 300.dp).widthIn(0.dp, 300.dp).padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(
                        text = date.month.name.plus(" ").plus(date.dayOfMonth).plus(", ").plus(date.year),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(20.dp),
                        textAlign = TextAlign.Center,
                    )
                    taskList(tasks.filter {
                        it.dueDate?.dayOfYear == date.dayOfYear && it.dueDate?.year == date.year
                    }, setTasks, tags, setTags, true, null, date)
                }
            }
        }
    }
}