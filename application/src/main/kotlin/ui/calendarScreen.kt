package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.opus.models.Task
import ui.components.dayPreview
import utils.minus
import utils.minusMonth
import utils.plus
import utils.plusMonth

@Composable
fun calendarScreen(toggleMenu: () -> Unit, showMenu: Boolean, tasks: List<Task>){
    var curDate by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) }
    var tempDate = curDate
    val tempMonth = curDate.month.name
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { toggleMenu() }, enabled = !showMenu) {
                Icon(if (showMenu) Icons.Default.CalendarMonth else Icons.Default.Menu, contentDescription = "Menu Button")
            }
            Text("Calendar")
            Row(Modifier.fillMaxWidth().width(IntrinsicSize.Max), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { curDate = curDate.minusMonth(1)}, enabled = true) {Icon(Icons.Default.ArrowBack, contentDescription = "Calender Right")}
                Text(tempMonth)
                IconButton(onClick = { curDate = curDate.plusMonth(1)}, enabled = true) {Icon(Icons.Default.ArrowForward, contentDescription = "Calender Left")}
            }
        }

        //Days of the week
        val daysOfWeek = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
        var day = 0
        LazyVerticalGrid(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth().padding(2.dp).padding(5.dp),
            content = {
                items(daysOfWeek.size) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White, //Card background color
                        )
                    ) {
                        Text(modifier = Modifier.fillMaxWidth(), text = daysOfWeek.get(day), textAlign = TextAlign.Center)
                    }
                    day += 1
                }
            }
        )

        // Insert days here
        val list = (1..42).map { it.toString() }
        tempDate -= (tempDate.dayOfMonth - 1)
        tempDate -= ((tempDate.dayOfWeek.ordinal + 1) % 7 )
        LazyVerticalGrid(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth().padding(2.dp).padding(5.dp),
            content = {
                items(list.size) {
                    val tasksOnDay = tasks.filter{
                        it.dueDate?.dayOfYear == tempDate.dayOfYear
                                && it.dueDate?.year == tempDate.year
                    }
                    dayPreview(tempDate, curDate.month.name, tasksOnDay)
                    tempDate += 1
                }
            }
        )
    }
}