package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Tag
import ui.components.TagButtonContent
import viewmodels.MainViewModel

@Composable
fun TagScreen(
    viewModel: MainViewModel
){
    val tags by viewModel.tags.collectAsStateWithLifecycle()
    Column(modifier = Modifier.padding(20.dp).fillMaxWidth().fillMaxHeight())  {
        tags.forEach { tag ->
            TagEntry(viewModel, tag)
        }
    }
}

@Composable
fun TagEntry(
    viewModel: MainViewModel,
    tag: Tag
) {
    var tagOptionState by remember(tag) { mutableStateOf(false) }

    Row {
        TagButtonContent(tag)
        Spacer(modifier = Modifier.weight(1f))
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
}