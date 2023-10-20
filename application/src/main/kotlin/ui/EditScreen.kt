package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.opus.models.Note
import org.opus.models.Tag
import org.opus.models.Task
import ui.components.NotePreview
import ui.components.taskList

@Composable
fun EditScreen(
    title: String,
    tasks: List<Task>,
    setTasks: (List<Task>) -> Unit,
    showMenu: Boolean,
    toggleMenu: () -> Unit,
    tags: List<Tag>,
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
        // Task List
        taskList(tasks.filter { !it.completed }, setTasks, tags, currentTag)
        Spacer(modifier = Modifier.size(30.dp))
        // Insert notes here
        LazyVerticalGrid(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            columns = GridCells.Adaptive(minSize = 240.dp),
            content = {
                items(notes.size) { NotePreview(notes[it], setNotes, tags, currentTag) }
            }
        )
    }
}