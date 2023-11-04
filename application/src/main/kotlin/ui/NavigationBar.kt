package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import api.ApiClient
import kotlinx.coroutines.launch
import org.models.opus.models.Colour
import org.models.opus.models.Tag

@Composable

fun NavigationBar(
    tags: List<Tag>,
    setTags: (List<Tag>) -> Unit,
    setScreen: (String) -> Unit,
    toggleMenu: () -> Unit
) {
    val addTagFocusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxHeight().width(IntrinsicSize.Min)) {
        Column(horizontalAlignment = Alignment.Start) {
            IconButton(onClick = { toggleMenu() }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu Button")
            }

            TextButton(onClick = { setScreen("All") }, modifier = Modifier.fillMaxWidth()) {
                Text("All", textAlign = TextAlign.Left, modifier = Modifier.fillMaxWidth())
            }

            TextButton(onClick = { setScreen("Calendar") }, modifier = Modifier.fillMaxWidth()) {
                Text("Calendar", textAlign = TextAlign.Left, modifier = Modifier.fillMaxWidth())
            }

            Divider(color = Color.Black, thickness = 1.dp)

            val moreStates = remember(tags) { mutableStateMapOf<Tag, Boolean>() }
            // Display all tags
            tags.forEach { tag ->
                LaunchedEffect(Unit) {
                    moreStates[tag] = false
                }

                Row {
                    TextButton(onClick = { setScreen(tag.title) }, modifier = Modifier.width(IntrinsicSize.Max)) {
                        Text(tag.title, textAlign = TextAlign.Left, modifier = Modifier.fillMaxWidth())
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { moreStates[tag] = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Tag Menu Button")
                        DropdownMenu(
                            expanded = moreStates[tag] == true, onDismissRequest = { moreStates[tag] = false }) {
                            DropdownMenuItem(onClick = {
                                coroutineScope.launch {
                                    setTags(ApiClient.getInstance().deleteTag(0, tag.id))
                                }
                            }) {
                                Text("Delete Tag")
                            }

                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(200.dp)) {
                IconButton(
                    onClick = { addTagFocusRequester.requestFocus() },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Tag")
                }
                TextField(
                    value = text,
                    placeholder = { Text("New tag") },
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
                                coroutineScope.launch {
                                    setTags(ApiClient.getInstance().postTag(0, tag))
                                }
                                text = ""
                            }
                            true
                        }
                )
            }
        }
    }
}