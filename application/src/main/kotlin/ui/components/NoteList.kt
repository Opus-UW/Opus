package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.models.opus.models.Note
import org.models.opus.models.Tag

@Composable
fun noteList(
    notes: List<Note>,
    setNotes: (List<Note>) -> Unit,
    tags: List<Tag>,
    setTags: (List<Tag>) -> Unit,
    currentTag: Tag?
) {
//    val listState = rememberLazyGridState()
    LazyVerticalGrid(
//        state = listState,
//        modifier = Modifier.SimpleVerticalScrollbar(listState),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        columns = GridCells.Adaptive(minSize = 240.dp),
        content = {
            items(notes.size) { NotePreview(notes[it], setNotes, tags, setTags, currentTag) }
        }
    )
}