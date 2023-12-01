package ui.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import viewmodels.MainViewModel

@Composable
fun DateDialog(
    viewModel: MainViewModel,
    date: kotlinx.datetime.LocalDateTime,
    showDateDialog: Boolean,
    setDateDialog: (Boolean) -> Unit,
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val currentTag by viewModel.currentTag.collectAsStateWithLifecycle()
    var taskFilter = tasks.filter { if (currentTag != null) it.tags.contains(currentTag) else true }


    if (showDateDialog) {
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
                    taskList(
                        viewModel,
                        taskFilter.filter {
                        it.dueDate?.dayOfYear == date.dayOfYear && it.dueDate?.year == date.year
                    }, true, date)
                }
            }
        }
    }
}