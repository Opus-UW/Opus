package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Tag
import ui.components.*
import ui.theme.md_theme_dark_tags
import ui.theme.md_theme_light_tags
import utils.toColour
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagEntry(
    viewModel: MainViewModel,
    tag: Tag
) {
    val darkTheme by viewModel.darkTheme.collectAsStateWithLifecycle()
    val (color, setColor) = remember(tag) { mutableStateOf(if (darkTheme == true) md_theme_dark_tags[tag.colour.ordinal] else md_theme_light_tags[tag.colour.ordinal] ) }
    var name by remember(tag) { mutableStateOf(tag.title) }
    var isTagFocused by remember(tag) { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val textFieldFocusRequester = remember(tag) { FocusRequester() }


    Box (modifier = Modifier.fillMaxWidth()
        .onFocusChanged {
            isTagFocused = it.hasFocus
            if (!isTagFocused){
                if (color.toColour() != tag.colour || name != tag.title){
                    viewModel.updateTag(name, color.toColour(), tag)
                }
            }
        }
    ){
        Column {
            if (isTagFocused){
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row (verticalAlignment = Alignment.CenterVertically){
                chooseColor(viewModel, color, setColor, 30.dp)
                Spacer(modifier = Modifier.width(10.dp))
                val interactionSource = remember { MutableInteractionSource() }
                val visualTransformation = VisualTransformation.None
                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    interactionSource = interactionSource,
                    visualTransformation = visualTransformation,
                    readOnly = !(isTagFocused),
                    textStyle = LocalTextStyle.current.merge(
                        TextStyle(
                            color = LocalContentColor.current
                        )
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key != Key.Enter) return@onKeyEvent false
                            if (keyEvent.type == KeyEventType.KeyUp) {
                                focusManager.clearFocus()
                            }
                            true
                        }
                        .focusRequester(textFieldFocusRequester).weight(1f),
                    singleLine = true
                ) { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = name,
                        interactionSource = interactionSource,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        enabled = true,
                        innerTextField = innerTextField,
                        singleLine = true,
                        visualTransformation = visualTransformation,
                        contentPadding = PaddingValues(0.dp)
                    )
                }
                IconButton(onClick = {viewModel.deleteTag(tag)}){
                    Icon(Icons.Default.Delete, contentDescription = "Delete Tag")
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            if (isTagFocused){
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)
            }
        }
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