package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Colour
import org.models.opus.models.Tag
import viewmodels.MainViewModel

@Composable
fun NavigationContent(
    viewModel: MainViewModel
){
    val tags by viewModel.tags.collectAsStateWithLifecycle()

    ModalDrawerSheet {
        Text("General", modifier = Modifier.padding(16.dp))
        NavigationEntry(viewModel, null)

//        TextButton(onClick = {  }, modifier = Modifier.fillMaxWidth()) {
//            Text("Calendar", textAlign = TextAlign.Left, modifier = Modifier.fillMaxWidth())
//        }

        Divider(color = Color.Black, thickness = 1.dp)
        Text("Tags", modifier = Modifier.padding(16.dp))
        tags.forEach{
                NavigationEntry(viewModel, it)
        }

        createNewTag(viewModel)
    }
}

@Composable
fun NavigationEntry(
    viewModel: MainViewModel,
    tag: Tag?
){
    var tagOptionState by remember { mutableStateOf(false) }
    val title = tag?.title ?: "All"


    Row {
        TextButton(onClick = { viewModel.setCurrentTag(tag)} , modifier = Modifier.weight(1f)) {
            Text(title ?: "Null", textAlign = TextAlign.Left, modifier = Modifier.fillMaxWidth())
        }

        if (tag != null){
            //Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { tagOptionState = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Tag Menu Button")
                DropdownMenu(
                    expanded = tagOptionState, onDismissRequest = { tagOptionState = false }) {
                    DropdownMenuItem(onClick = {
                        viewModel.deleteTag(tag)
                    }) {
                        androidx.compose.material.Text("Delete Tag")
                    }

                }
            }
        }
    }
}

@Composable
fun createNewTag(
    viewModel: MainViewModel
){
    val addTagFocusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf("") }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        IconButton(
            onClick = { addTagFocusRequester.requestFocus() },
            modifier = Modifier.size(20.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Tag")
        }
        TextField(
            value = text,
            placeholder = { androidx.compose.material.Text("New tag") },
            onValueChange = { text = it },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                disabledTextColor = Color.Black,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .focusRequester(addTagFocusRequester)
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key != Key.Enter) return@onKeyEvent false
                    if (keyEvent.type == KeyEventType.KeyUp) {
                        val tag = Tag(text, Colour(237, 217, 229))
                        viewModel.createTag(tag)
                        text = ""
                    }
                    true
                }
        )
    }
}