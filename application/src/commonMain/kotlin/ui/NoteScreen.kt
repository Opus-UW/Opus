package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Note
import ui.components.EditNoteDialog
import ui.components.NotePreview
import viewmodels.MainViewModel

@Composable
fun NoteScreen(
    viewModel: MainViewModel
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()

    Column(modifier = Modifier.padding(20.dp).fillMaxWidth().fillMaxHeight()) {
        AddNote(viewModel)
        Spacer(modifier = Modifier.height(5.dp))
        LazyVerticalGrid(
            state = listState,
//                modifier = Modifier.SimpleVerticalScrollbar(listState),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            columns = GridCells.Adaptive(minSize = 240.dp),
            content = {
                items(notes.size) { note ->
                    NotePreview(viewModel, notes[note])
                }
            }
        )
    }
}

@Composable
fun AddNote(
    viewModel: MainViewModel
){
    var note = Note("", "", listOf(), -1)
    val tags by viewModel.tags.collectAsStateWithLifecycle()
    var editNote by remember(note) { mutableStateOf(false) }
    val (title, setTitle) = remember(note) { mutableStateOf(note.title) }
    val tagStatus = remember(tags) { mutableStateMapOf(*tags.map { it to false }.toTypedArray()) }
    val state = rememberRichTextState()

    Row (modifier = Modifier.fillMaxWidth()){
        Spacer(modifier = Modifier.weight(0.5f))
        ElevatedButton(
            onClick = {editNote = true},
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier.weight(1f)
        ){
            Row {
                Text("Add a note...", fontSize = 15.sp)
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }

    if (editNote) {
        val newState = rememberRichTextState()
        EditNoteDialog(
            viewModel,
            { editNote = false; },
            title,
            setTitle,
            newState,
            state,
            note,
            tagStatus
        )
        note = Note("", "", listOf(), -1)
    }
}