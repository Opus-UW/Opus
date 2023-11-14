package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.models.opus.models.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayPreview(previewDate: LocalDateTime, previewMonth: String,
               tasks: List<Task>,
               setShowDateDialog: (Boolean) -> Unit,
               setSelectedDate: (LocalDateTime) -> Unit) {
    val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val cardBgColor = if (todayDate.date == previewDate.date && todayDate.month == previewDate.month && todayDate.year == previewDate.year)
        Color(0xFFE0FFFF) else Color.White
    val cardTextColor = if (previewDate.month.name == previewMonth) Color.Black else Color.Gray

    Box(
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 6.dp
//        ),
        modifier = Modifier.background(cardBgColor)
            .border(1.dp, Color.LightGray)
            .aspectRatio(1.25f)
            .heightIn(0.dp, 30.dp)
            .clickable ( onClick = {
                setSelectedDate(previewDate)
                setShowDateDialog(true)
            })
    ) {
        Column {
            TextField(
                value = "${previewDate.dayOfMonth}",
                placeholder = { androidx.compose.material.Text("Title") },
                onValueChange = {},
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = cardTextColor,
                    //disabledTextColor = Color.Black,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                enabled = false
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    items(tasks.size) {
                        if (tasks[it].completed) {
                            Text(tasks[it].action, style = TextStyle(textDecoration = TextDecoration.LineThrough))
                        } else Text(tasks[it].action)
                    }
                }
            )
        }
    }
}
