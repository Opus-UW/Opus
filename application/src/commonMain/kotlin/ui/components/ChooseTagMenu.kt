package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Colour
import org.models.opus.models.Tag
import viewmodels.MainViewModel

@Composable
fun ChooseTagMenu(
    viewModel: MainViewModel,
    showTags: Boolean,
    setShowTags: (Boolean) -> Unit,
    tagStatus: SnapshotStateMap<Tag, Boolean>
) {
    val tags by viewModel.tags.collectAsStateWithLifecycle()

    var newTag by remember(tags) { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    val textFieldFocusRequester = remember { FocusRequester() }

    DropdownMenu(
        expanded = showTags,
        onDismissRequest = {
            setShowTags (false)
        }
    ) {
        Column (horizontalAlignment = Alignment.Start) {
            tags.forEach { tag ->
                Row (verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { tagStatus[tag] = !tagStatus[tag]!! }) {
                        Icon(
                            if (tagStatus[tag] == true) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                            contentDescription = "Not Selected"
                        )
                    }
                    Text(tag.title)
                }
            }

            Row (verticalAlignment = Alignment.CenterVertically){
                IconButton(onClick = {textFieldFocusRequester.requestFocus()}){
                    Icon(Icons.Default.Add, contentDescription = "Add Tag")
                }
                TextField(
                    value = newTag,
                    placeholder = { Text("Add tag") },
                    onValueChange = { newTag = it },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        disabledTextColor = Color.Black,
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.onKeyEvent { keyEvent ->
                        if (keyEvent.key != Key.Enter) return@onKeyEvent false
                        if (keyEvent.type == KeyEventType.KeyUp) {
                            val tag = Tag(newTag, Colour(237, 217, 229))
                            viewModel.createTag(tag)
                            text = ""
                        }
                        true
                    }
                        .focusRequester(textFieldFocusRequester)
                )
            }
        }
    }
}