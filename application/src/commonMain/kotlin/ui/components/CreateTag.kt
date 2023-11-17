package ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import org.models.opus.models.Colour
import org.models.opus.models.Tag
import viewmodels.MainViewModel

@Composable
fun createNewTag(
    viewModel: MainViewModel
){
    val addTagFocusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf("") }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(
            onClick = { addTagFocusRequester.requestFocus() },
            modifier = Modifier.size(20.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Tag")
        }
        Spacer(modifier = Modifier.width(5.dp))
        TextField(
            value = text,
            placeholder = { Text("New tag") },
            onValueChange = { text = it },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
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
                }.weight(1f)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Box(modifier = Modifier
            .size(40.dp)
            .border(3.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .padding(1.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.tertiary)
        )
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Composable
fun chooseTagColor(
    viewModel: MainViewModel
){

}