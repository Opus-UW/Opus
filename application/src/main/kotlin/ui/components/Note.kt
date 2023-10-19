package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotePreview(){
    var editNote by remember { mutableStateOf(false) }
    var (title, setTitle) = remember { mutableStateOf("") }
    val state = rememberRichTextState()
    var (html, setHtml) = remember { mutableStateOf("") }

    ElevatedCard(
        onClick = { editNote = true },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 200.dp)
    ) {
        Column{
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
                enabled = true
            )
        }
    }
    if (editNote){
        html = state.toHtml()
        val newState = rememberRichTextState()
        LaunchedEffect(editNote) {
            newState.setHtml(html)
        }
        EditNoteDialog({ editNote = false }, title, setTitle, newState, setHtml, state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteDialog(onDismissRequest: () -> Unit, title: String, setTitle: (String) -> Unit, newState: RichTextState, setHtml: (String) -> Unit, state: RichTextState) {
    var newTitle by remember { mutableStateOf(title) }

    Dialog(onDismissRequest = { setTitle(newTitle); state.setHtml(newState.toHtml()); onDismissRequest() },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
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
            Row (modifier = Modifier.fillMaxWidth()){
                IconButton(onClick = {newState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))}) {Icon(Icons.Default.FormatBold, contentDescription = "Bold Text")}
                IconButton(onClick = {newState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))}) {Icon(Icons.Default.FormatItalic, contentDescription = "Italic Text")}
                IconButton(onClick = {newState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))}) {Icon(Icons.Default.FormatUnderlined, contentDescription = "Underlined Text")}
            }
            RichTextEditor(
                state = newState,
                modifier = Modifier.fillMaxWidth(),
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
