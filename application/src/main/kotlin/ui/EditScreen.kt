package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.opus.models.Task
import ui.components.NotePreview
import ui.components.taskList

@Composable
fun EditScreen(title: String, tasks: List<Task>, setTasks: (List<Task>) -> Unit, toggleMenu: () -> Unit, tags: MutableList<String>){
    Column {
        // Title + Menu Button
        Row (verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {toggleMenu()}) {
                Icon (Icons.Default.Menu, contentDescription = "Menu Button")
            }
            Text (title)
        }
        // Task List
        taskList(tasks, setTasks, tags)
        Spacer(modifier = Modifier.size(30.dp))
        // Insert notes here
        NotePreview()
    }
}