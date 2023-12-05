package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Tag
import ui.deleteTagMenu
import ui.theme.isDarkTheme
import ui.theme.md_theme_dark_tags
import ui.theme.md_theme_light_tags
import utils.toColour
import viewmodels.MainViewModel

@Composable
fun ChooseTagMenu(
    viewModel: MainViewModel,
    showTags: Boolean,
    setShowTags: (Boolean) -> Unit,
    tagStatus: SnapshotStateMap<Tag, Boolean>
) {
    val tags by viewModel.tags.collectAsStateWithLifecycle()
    val darkTheme by viewModel.darkTheme.collectAsStateWithLifecycle()

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
                    TagButtonContent(darkTheme ?: false, tag)
                    Spacer(modifier = Modifier.weight(1f))
                    deleteTagMenu(viewModel, tag)
                }
            }
            createNewTag(viewModel)
        }
    }
}

@Composable
fun createNewTag(viewModel: MainViewModel){
    var newTag by remember { mutableStateOf("") }
    val textFieldFocusRequester = remember { FocusRequester() }
    val (color, setColor) = remember { mutableStateOf(Color.Transparent) }

    Row (verticalAlignment = Alignment.CenterVertically){
        IconButton(onClick = {textFieldFocusRequester.requestFocus()}){
            Icon(Icons.Default.Add, contentDescription = "Add Tag")
        }
        Spacer(modifier = Modifier.width(5.dp))
        chooseColor(viewModel, color, setColor, 40.dp)
        Spacer(modifier = Modifier.width(10.dp))
        TextField(
            value = newTag,
            placeholder = { Text("Add a tag...") },
            onValueChange = { newTag = it },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier.onKeyEvent { keyEvent ->
                if (keyEvent.key != Key.Enter) return@onKeyEvent false
                if (keyEvent.type == KeyEventType.KeyUp) {
                    val tag = Tag(newTag, color.toColour())
                    print (tag)
                    viewModel.createTag(tag)
                    newTag = ""
                    setColor(Color.Transparent)
                }
                true
            }
                .focusRequester(textFieldFocusRequester)
        )
    }
}

@Composable
fun chooseColor(
    viewModel: MainViewModel,
    currentColor: Color,
    setColor: (Color) -> Unit,
    size: Dp
){
    val darkTheme by viewModel.darkTheme.collectAsStateWithLifecycle()
    val (showColors, setShowColors) = remember { mutableStateOf(false) }
    if (currentColor == Color(0, 0,0 ,0)){
        if (darkTheme == true){
            setColor(md_theme_dark_tags[0])
        }
        else{
            setColor(md_theme_light_tags[0])
        }
    }

    LaunchedEffect(darkTheme){
        if (darkTheme == true){
            setColor(md_theme_dark_tags[currentColor.toColour().ordinal])
        }
        else{
            setColor(md_theme_light_tags[currentColor.toColour().ordinal])
        }
    }

    displayColor(currentColor, Color.Black, size) { setShowColors(true) }

    DropdownMenu(
        expanded = showColors,
        onDismissRequest = {
            setShowColors (false)
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
    ){
        Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)){
            (if (darkTheme == true) md_theme_dark_tags else md_theme_light_tags).forEach{color ->
                displayColor(color, currentColor, size){setColor(color)}
            }
        }
    }
}

fun Modifier.conditionalBorder (condition : Boolean, color: Color) : Modifier {
    return if (condition) {
        then(Modifier.border(2.dp,  color, CircleShape))
    } else {
        this
    }
}

@Composable
fun displayColor(
    color: Color,
    currentColor: Color,
    size: Dp,
    action: () -> Unit
){
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .conditionalBorder(currentColor == color, MaterialTheme.colorScheme.outline)
            .background(color)
            .noRippleClickable { action() }
    )
}