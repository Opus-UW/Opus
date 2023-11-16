package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
        onClick = { editNote = true },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 200.dp).alpha(if (editNote) 0f else 1f),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray, //Card background color
        )
    ) {
        Column {
            // Title
            TextField(
                value = title,
                placeholder = { androidx.compose.material.Text("Title") },
                onValueChange = { title = it },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    disabledTextColor = Color.Black,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                enabled = false
            )
            // Body
            RichTextEditor(
                state = state,
                modifier = Modifier.fillMaxWidth(),
                colors = RichTextEditorDefaults.richTextEditorColors(
                    textColor = Color.Black,
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
                    viewModel.updateNote(note, title = newTitle, body = newState.toHtml(), tags = newTaskTags.toList())
                }
                else {
                    val newNote = Note(title, newState.toHtml(), newTaskTags)
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
            shape = RoundedCornerShape(16.dp)
        ) {
            Row {
                TextField(
                    value = newTitle,
                    placeholder = { androidx.compose.material.Text("Title") },
                    onValueChange = { newTitle = it },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        disabledTextColor = Color.Black,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                val (showTags, setShowTags) = remember(tags) { mutableStateOf(false) }
                var rootPos by remember { mutableStateOf(Offset.Zero) }
                androidx.compose.material.TextButton(onClick = { setShowTags(true) },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        rootPos = coordinates.positionInRoot()
                    }) {
                    androidx.compose.material.Icon(Icons.Default.Sell, contentDescription = "Tags")
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
                IconButton(onClick = { newState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) }) {
                    Icon(
                        Icons.Default.FormatBold,
                        contentDescription = "Bold Text"
                    )
                }
                IconButton(onClick = { newState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) }) {
                    Icon(
                        Icons.Default.FormatItalic,
                        contentDescription = "Italic Text"
                    )
                }
                IconButton(onClick = { newState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline)) }) {
                    Icon(
                        Icons.Default.FormatUnderlined,
                        contentDescription = "Underlined Text"
                    )
                }
                IconButton(onClick = { newState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) }) {
                    Icon(
                        Icons.Default.FormatStrikethrough,
                        contentDescription = "Strikethrough Text"
                    )
                }
                IconButton(onClick = { newState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Start)) }) {
                    Icon(
                        Icons.Default.FormatAlignLeft,
                        contentDescription = "Left Align Text"
                    )
                }
                IconButton(onClick = { newState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center)) }) {
                    Icon(
                        Icons.Default.FormatAlignCenter,
                        contentDescription = "Center Align Text"
                    )
                }
                IconButton(onClick = { newState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.End)) }) {
                    Icon(
                        Icons.Default.FormatAlignRight,
                        contentDescription = "Right Align Text"
                    )
                }
                IconButton(onClick = { newState.toggleOrderedList() }) {
                    Icon(
                        Icons.Default.FormatListNumbered,
                        contentDescription = "Numbered List Text"
                    )
                }
                IconButton(onClick = { newState.toggleUnorderedList() }) {
                    Icon(
                        Icons.Default.FormatListBulleted,
                        contentDescription = "Bullet List Text"
                    )
                }
            }
            RichTextEditor(
                state = newState,
                modifier = Modifier
                    .fillMaxWidth()
                    .onKeyEvent {
                        if (it.isCtrlPressed && it.key == Key.B && it.type == KeyEventType.KeyUp) {
                            newState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            true
                        } else if (it.isCtrlPressed && it.key == Key.I && it.type == KeyEventType.KeyUp) {
                            newState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                            true
                        } else if (it.isCtrlPressed && it.key == Key.U && it.type == KeyEventType.KeyUp) {
                            newState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                            true
                        } else if (it.isAltPressed && it.isShiftPressed && it.key == Key.Five && it.type == KeyEventType.KeyUp) {
                            newState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                            true
                        } else if (it.isCtrlPressed && it.isShiftPressed && it.key == Key.L && it.type == KeyEventType.KeyUp) {
                            newState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Start))
                            true
                        } else if (it.isCtrlPressed && it.isShiftPressed && it.key == Key.E && it.type == KeyEventType.KeyUp) {
                            newState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
                            true
                        } else if (it.isCtrlPressed && it.isShiftPressed && it.key == Key.R && it.type == KeyEventType.KeyUp) {
                            newState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.End))
                            true
                        } else if (it.isCtrlPressed && it.isShiftPressed && it.key == Key.Seven && it.type == KeyEventType.KeyUp) {
                            newState.toggleOrderedList()
                            true
                        } else if (it.isCtrlPressed && it.isShiftPressed && it.key == Key.Eight && it.type == KeyEventType.KeyUp) {
                            newState.toggleUnorderedList()
                            true
                        } else {
                            // let other handlers receive this event
                            false
                        }
                    },
                colors = RichTextEditorDefaults.richTextEditorColors(
                    textColor = Color.Black,
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


