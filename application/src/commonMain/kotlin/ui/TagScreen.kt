package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Tag
import ui.components.SimpleVerticalScrollbar
import ui.components.TagButtonContent
import ui.components.createNewTag
import viewmodels.MainViewModel

@Composable
fun TagScreen(
    viewModel: MainViewModel
){
    val tags by viewModel.tags.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    Column (modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(20.dp)){
        LazyColumn(
            state = listState,
            modifier = Modifier.SimpleVerticalScrollbar(listState).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = tags,
                key = { tag ->
                    tag.id
                }
            ) { tag ->
                TagEntry(viewModel, tag)
            }
        }
    }
}

@Composable
fun TagEntry(
    viewModel: MainViewModel,
    tag: Tag
) {

    Row {
        TagButtonContent(tag)
        Spacer(modifier = Modifier.weight(1f))
        deleteTagMenu(viewModel, tag)
    }
}

@Composable
fun deleteTagMenu(
    viewModel: MainViewModel,
    tag: Tag
){
    var tagOptionState by remember(tag) { mutableStateOf(false) }

    IconButton(onClick = { tagOptionState = true }) {
        Icon(Icons.Default.MoreVert, contentDescription = "Tag Menu Button")
        DropdownMenu(
            expanded = tagOptionState, onDismissRequest = { tagOptionState = false }) {
            DropdownMenuItem(onClick = {
                viewModel.deleteTag(tag)
            },
                text = {
                    Text("Delete Tag")
                }
            )

        }
    }
}