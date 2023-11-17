package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
fun DayPreview(previewDatee: LocalDateTime, previewMonth: String,
               tasks: List<Task>,
               setShowDateDialog: (Boolean) -> Unit,
               setSelectedDate: (LocalDateTime) -> Unit,
               compact: Boolean) {
    val previewDate by remember(previewMonth) { mutableStateOf(previewDatee) }
    val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val cardBgColor = if (todayDate.date == previewDate.date && todayDate.month == previewDate.month && todayDate.year == previewDate.year)
        MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    val cardTextColor = if (previewDate.month.name == previewMonth) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer
    val aspectRatio = if (compact) 0.8f else 1.25f

    Box(
        modifier = Modifier.background(cardBgColor)
            .border(1.dp, MaterialTheme.colorScheme.outline)
            .aspectRatio(aspectRatio)
            .heightIn(0.dp, 30.dp)
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
