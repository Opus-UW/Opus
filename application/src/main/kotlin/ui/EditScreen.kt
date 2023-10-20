package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.opus.models.Note
import org.opus.models.Tag
import org.opus.models.Task
import ui.components.noteList
import ui.components.taskList

@Composable
fun EditScreen(
    title: String,
    tasks: List<Task>,
    setTasks: (List<Task>) -> Unit,
    showMenu: Boolean,
    toggleMenu: () -> Unit,
    tags: List<Tag>,
    setTags: (List<Tag>) -> Unit,
    notes: List<Note>,
    setNotes: (List<Note>) -> Unit,
    currentTag: Tag?
) {
    Column {
        // Title + Menu Button
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { toggleMenu() }, enabled = !showMenu) {
                Icon(if (showMenu) Icons.Default.Book else Icons.Default.Menu, contentDescription = "Menu Button")
            }
            Text(title)
        }
        var showCompleted by remember { mutableStateOf(false) }
        // Task List
        Box (modifier = Modifier.fillMaxWidth().fillMaxHeight(if (showCompleted) 0.3f else 0.4f)){
            taskList(tasks.filter { !it.completed }, setTasks, tags, setTags,true, currentTag)
        }
        Spacer(modifier = Modifier.size(10.dp))
        Row (verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = {showCompleted = !showCompleted}){
                if (showCompleted){
                    Icon(Icons.Default.ExpandMore, contentDescription = "Show Completed Tasks")
                }
                else {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Show Completed Tasks")
                }
            }
            Text("Completed")
            Spacer(modifier = Modifier.size(10.dp))
            Text(tasks.filter { it.completed }.size.toString())
        }
        Divider(color = Color.Black, thickness = 1.dp)
        if (showCompleted){
            Box (modifier = Modifier.fillMaxWidth().fillMaxHeight(0.2f)){
                Spacer(modifier = Modifier.size(10.dp))
                taskList(tasks.filter { it.completed }, setTasks, tags, setTags,false, currentTag)
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
        // Insert notes here
        Box (modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            noteList(notes, setNotes, tags, setTags, currentTag)
        }
    }
}