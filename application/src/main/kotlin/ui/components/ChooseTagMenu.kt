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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import api.ApiClient
import kotlinx.coroutines.launch
import org.opus.models.Colour
import org.opus.models.Tag

@Composable
fun ChooseTagMenu(
    showTags: Boolean,
    setShowTags: (Boolean) -> Unit,
    pos: Offset,
    tags: List<Tag>,
    setTags: (List<Tag>) -> Unit,
    tagStatus: SnapshotStateMap<Tag, Boolean>
) {
    val focusManager = LocalFocusManager.current
    var newTag by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val textFieldFocusRequester = remember(tags) { FocusRequester() }

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
                            coroutineScope.launch {
                                setTags(ApiClient.getInstance().postTag(0, tag))
                            }
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