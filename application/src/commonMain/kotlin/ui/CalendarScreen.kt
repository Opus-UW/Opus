package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import ui.components.DateDialog
import ui.components.DayPreview
import utils.minus
import utils.plus
import viewmodels.MainViewModel

@Composable
fun CalendarScreen(
    viewModel: MainViewModel
) {
    val curDate by viewModel.curDate.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val currentTag by viewModel.currentTag.collectAsStateWithLifecycle()

    var taskFilter = tasks.filter { if (currentTag != null) it.tags.contains(currentTag) else true }

    val (showDateDialog, setShowDateDialog) = remember { mutableStateOf(false) }
    val (selectedDate, setSelectedDate) = remember { mutableStateOf(curDate) }

    BoxWithConstraints (
        modifier = Modifier.fillMaxSize()
    ) {
        val height = maxHeight
        val dayHeight = (height - 55.dp) / 6
        Column {

            //Days of the week
            val daysOfWeek = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
            LazyVerticalGrid(
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth().padding(2.dp).padding(5.dp).height(25.dp),
                content = {
                    items(daysOfWeek.size) {
                        Card (shape = RectangleShape){
                            Text(modifier = Modifier.fillMaxWidth(), text = daysOfWeek.get(it), textAlign = TextAlign.Center)
                        }
                    }
                }
            )

            var tempDate = curDate
            val list = (1..42).map { it.toString() }
            tempDate -= (tempDate.dayOfMonth - 1)
            tempDate -= ((tempDate.dayOfWeek.ordinal + 1) % 7 )
            LazyVerticalGrid(
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth().padding(2.dp).padding(5.dp).heightIn(0.dp, height),
                content = {
                    items(list.size, key = { tempDate + it }) {idx ->
                        val tasksOnDay = taskFilter.filter{
                            it.dueDate?.dayOfYear == ((tempDate + idx).dayOfYear)
                                    && it.dueDate?.year == (tempDate + idx).year
                        }
                        DayPreview((tempDate + idx), curDate.month.name, tasksOnDay, setShowDateDialog, setSelectedDate, dayHeight)
                    }
                }
            )

            DateDialog(viewModel, selectedDate, showDateDialog, setShowDateDialog)
        }
    }
}