package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.models.opus.models.Tag
import ui.components.NotePreview
import ui.components.OpusTopAppBar
import ui.components.SimpleVerticalScrollbar
import viewmodels.MainViewModel

@Composable
fun NoteScreen(
    viewModel: MainViewModel
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()

    Column(modifier = Modifier.padding(5.dp)) {
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