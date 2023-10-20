package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ui.components.dayPreview
import utils.minus
import utils.minusMonth
import utils.plus
import utils.plusMonth

@Composable
fun calendarScreen(toggleMenu: () -> Unit, showMenu: Boolean){
    var curDate by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) }
    var tempDate = curDate
    val tempMonth = curDate.month.name
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { toggleMenu() }, enabled = !showMenu) {
                Icon(if (showMenu) Icons.Default.CalendarMonth else Icons.Default.Menu, contentDescription = "Menu Button")
            }
            Text("Calendar     ")
            TextButton(onClick = { curDate = curDate.minusMonth(1)}) {Text("<- Prev")}
            Text(tempMonth)
            TextButton(onClick = { curDate = curDate.plusMonth(1)}) {Text("Next ->")}
        }

        // Insert days here
        val list = (1..42).map { it.toString() }
        tempDate -= (tempDate.dayOfMonth - 1)
        tempDate -= ((tempDate.dayOfWeek.ordinal + 1) % 7 )
        LazyVerticalGrid(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            columns = GridCells.Fixed(7),
            content = {
                items(list.size) {
                    if (tempMonth == tempDate.month.name)
                        dayPreview("${tempDate.dayOfWeek} ${tempDate.dayOfMonth}")
                    else
                        dayPreview("x ${tempDate.dayOfWeek} ${tempDate.dayOfMonth}")
                    println(tempDate)
                    tempDate += 1
                }
            }
        )
    }
}