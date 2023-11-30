package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Note
import org.models.opus.models.Tag
import viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotePreview(
    viewModel: MainViewModel,
    note: Note
) {
    val tags by viewModel.tags.collectAsStateWithLifecycle()
    val currentTag by viewModel.currentTag.collectAsStateWithLifecycle()

    var editNote by remember(note) { mutableStateOf(false) }
    var (title, setTitle) = remember(note) { mutableStateOf(note.title) }
    val state = rememberRichTextState()
    val tagStatus = remember(tags) { mutableStateMapOf(*tags.map { it to false }.toTypedArray()) }

    LaunchedEffect(Unit) {
        note.tags.forEach { tag ->
            tagStatus[tag] = true
        }
        if (currentTag != null) {
            tagStatus[currentTag!!] = true
        }
    }
    LaunchedEffect(Unit) {
        state.setHtml(note.body)
    }

    // Note Base
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)),
        modifier = Modifier
            .size(width = 240.dp, height = 200.dp).alpha(if (editNote) 0f else 1f)
            .clickable(
            interactionSource = MutableInteractionSource(),
            indication = rememberRipple(),
            onClick = {editNote = true})
    ) {
        Column {
            // Title
            TextField(
                value = title,
                placeholder = { Text("Title") },
                onValueChange = { title = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledTextColor = LocalContentColor.current,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                enabled = false
            )
            // Body
            RichTextEditor(
                state = state,
                modifier = Modifier.fillMaxWidth(),
                colors = RichTextEditorDefaults.richTextEditorColors(
                    disabledTextColor = Color.Transparent,
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                readOnly = true
            )
        }
    }
    if (editNote) {
        val newState = rememberRichTextState()
        // Launched effect to only run on change
        LaunchedEffect(editNote) {
            newState.setHtml(state.toHtml())
        }
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
    }
}


data class keyboardShortcut(
    val name: String,
    val icon: ImageVector,
    val spanStyle: SpanStyle? = null,
    val paragraphStyle: ParagraphStyle? = null,
    val bulletList: Boolean = false,
    val numberList: Boolean = false,
    val ctrl: Boolean = false,
    val alt: Boolean = false,
    val shift: Boolean = false,
    val keys: List<Key>
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteDialog(
    viewModel: MainViewModel,
    onDismissRequest: () -> Unit,
    title: String,
    setTitle: (String) -> Unit,
    newState: RichTextState,
    state: RichTextState,
    note: Note,
    tagStatus: SnapshotStateMap<Tag, Boolean>
) {
    var newTitle by remember { mutableStateOf(title) }
    val tags by viewModel.tags.collectAsStateWithLifecycle()

    val keyboardShortcuts = listOf<keyboardShortcut>(
        keyboardShortcut(
            "Bold Text", Icons.Default.FormatBold, spanStyle = SpanStyle(fontWeight = FontWeight.Bold),
            ctrl = true, keys = listOf<Key>(Key.B)
        ),
        keyboardShortcut(
            "Italic Text", Icons.Default.FormatItalic, spanStyle = SpanStyle(fontStyle = FontStyle.Italic),
            ctrl = true, keys = listOf<Key>(Key.I)
        ),
        keyboardShortcut(
            "Underlined Text",
            Icons.Default.FormatUnderlined,
            spanStyle = SpanStyle(textDecoration = TextDecoration.Underline),
            ctrl = true,
            keys = listOf<Key>(Key.U)
        ),
        keyboardShortcut(
            "Strikethrough Text",
            Icons.Default.FormatStrikethrough,
            spanStyle = SpanStyle(textDecoration = TextDecoration.LineThrough),
            alt = true,
            shift = true,
            keys = listOf<Key>(Key.Five)
        ),
        keyboardShortcut(
            "Left Align Text",
            Icons.Default.FormatAlignLeft,
            paragraphStyle = ParagraphStyle(textAlign = TextAlign.Start),
            ctrl = true,
            shift = true,
            keys = listOf<Key>(Key.L)
        ),
        keyboardShortcut(
            "Center Align Text",
            Icons.Default.FormatAlignCenter,
            paragraphStyle = ParagraphStyle(textAlign = TextAlign.Center),
            ctrl = true,
            shift = true,
            keys = listOf<Key>(Key.E)
        ),
        keyboardShortcut(
            "Right Align Text",
            Icons.Default.FormatAlignRight,
            paragraphStyle = ParagraphStyle(textAlign = TextAlign.End),
            ctrl = true,
            shift = true,
            keys = listOf<Key>(Key.R)
        ),
        keyboardShortcut(
            "Numbered List Text", Icons.Default.FormatListNumbered, numberList = true,
            ctrl = true, shift = true, keys = listOf<Key>(Key.Seven)
        ),
        keyboardShortcut(
            "Bullet List Text", Icons.Default.FormatListBulleted, bulletList = true,
            ctrl = true, shift = true, keys = listOf<Key>(Key.Eight)
        )
    )

    Dialog(
        onDismissRequest = {
            if (title != newTitle || state.toHtml() != newState.toHtml()) {
                // Update tags
                val newTaskTags = mutableListOf<Tag>()
                tagStatus.forEach { entry ->
                    if (entry.value) {
                        newTaskTags.add(entry.key)
                    }
                }
                // Check if new note is to be created
                if (note.id != -1) {
                    setTitle(newTitle)
                    state.setHtml(newState.toHtml())
                    viewModel.updateNote(
                        note,
                        title = newTitle,
                        body = newState.toHtml(),
                        tags = newTaskTags.toList()
                    )
                } else {
                    val newNote = Note(newTitle, newState.toHtml(), newTaskTags)
                    viewModel.createNote(newNote)
                }
            }
            onDismissRequest()
        },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .defaultMinSize(minHeight = 400.dp)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row {
                TextField(
                    value = newTitle,
                    placeholder = { Text("Title") },
                    onValueChange = { newTitle = it },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                val (showTags, setShowTags) = remember(tags) { mutableStateOf(false) }
                var rootPos by remember { mutableStateOf(Offset.Zero) }
                TextButton(onClick = { setShowTags(true) },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        rootPos = coordinates.positionInRoot()
                    }) {
                    Icon(Icons.Default.Sell, contentDescription = "Tags")
                    ChooseTagMenu(viewModel, showTags, setShowTags, tagStatus)
                }
                IconButton(onClick = {
                    viewModel.deleteNote(note)
                }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Note"
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                keyboardShortcuts.forEach { shortcut ->
                    IconButton(
                        onClick =
                        {
                            if (shortcut.spanStyle != null) {
                                newState.toggleSpanStyle(shortcut.spanStyle)
                            } else if (shortcut.paragraphStyle != null) {
                                newState.toggleParagraphStyle(shortcut.paragraphStyle)
                            } else if (shortcut.bulletList) {
                                newState.toggleUnorderedList()
                            } else if (shortcut.numberList) {
                                newState.toggleOrderedList()
                            }
                        }

                    ) {
                        Icon(shortcut.icon, contentDescription = shortcut.name)
                    }

                }
            }
            RichTextEditor(
                state = newState,
                modifier = Modifier
                    .fillMaxWidth()
                    .onKeyEvent {
                        if (it.type == KeyEventType.KeyUp) {
                            keyboardShortcuts.forEach { shortcut ->
                                val ctrlPressed = (shortcut.ctrl && it.isCtrlPressed) || !shortcut.ctrl
                                val shiftPressed = (shortcut.shift && it.isShiftPressed) || !shortcut.shift
                                val altPressed = (shortcut.alt && it.isAltPressed) || !shortcut.alt
                                var keysPressed = true
                                shortcut.keys.forEach { key ->
                                    keysPressed = keysPressed && it.key == key
                                }
                                if (ctrlPressed && shiftPressed && altPressed && keysPressed) {
                                    if (shortcut.spanStyle != null) {
                                        newState.toggleSpanStyle(shortcut.spanStyle)
                                    } else if (shortcut.paragraphStyle != null) {
                                        newState.toggleParagraphStyle(shortcut.paragraphStyle)
                                    } else if (shortcut.bulletList) {
                                        newState.toggleUnorderedList()
                                    } else if (shortcut.numberList) {
                                        newState.toggleOrderedList()
                                    }
                                }
                            }
                            true
                        } else {
                            // let other handlers receive this event
                            false
                        }
                    },
                colors = RichTextEditorDefaults.richTextEditorColors(
                    disabledTextColor = Color.Transparent,
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
    }
}


