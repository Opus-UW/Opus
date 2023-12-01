package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.models.opus.models.Task

@Composable
fun DayPreview(previewDate: LocalDateTime, previewMonth: String,
               tasks: List<Task>,
               setShowDateDialog: (Boolean) -> Unit,
               setSelectedDate: (LocalDateTime) -> Unit,
               height: Dp) {
    val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val cardBgColor = if (todayDate.date == previewDate.date && todayDate.month == previewDate.month && todayDate.year == previewDate.year)
        MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
    val cardTextColor = if (previewDate.month.name == previewMonth) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    Box(
        modifier = Modifier.background(cardBgColor)
            .border(1.dp, MaterialTheme.colorScheme.outline)
            .height(height)
            .clickable ( onClick = {
                setSelectedDate(previewDate)
                setShowDateDialog(true)
            })
    ) {
        Column {
            Box(
                modifier = Modifier.padding(5.dp)
            ) {Text(text="${previewDate.dayOfMonth}", color=cardTextColor)}
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                content = {
                    items(tasks.size) {
                        if (tasks[it].completed) {
                            Box(
                                modifier = Modifier.background(color=MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(5.dp))
                            ) {
                                Text(tasks[it].action,
                                    style = TextStyle(textDecoration = TextDecoration.LineThrough, color = MaterialTheme.colorScheme.onSecondary),
                                    modifier=Modifier.padding(horizontal = 5.dp))
                            }
                        } else Box(
                            modifier = Modifier.background(color= MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(5.dp))
                        ) {
                            Text(tasks[it].action, modifier=Modifier.padding(horizontal = 5.dp), color = MaterialTheme.colorScheme.onTertiary)
                        }
                    }
                }
            )
        }
    }
}
