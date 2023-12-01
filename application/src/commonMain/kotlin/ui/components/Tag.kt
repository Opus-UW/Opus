package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Tag
import utils.toColor
import utils.toColour
import viewmodels.MainViewModel

@Composable
fun TagButton(
    viewModel: MainViewModel,
    tag: Tag
) {
    val currentTag by viewModel.currentTag.collectAsStateWithLifecycle()
    ElevatedButton(
        onClick = {
            if (currentTag == tag) {
                viewModel.setCurrentTag(null)
            } else {
                viewModel.setCurrentTag(tag)
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (tag == currentTag) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceColorAtElevation(
                1.dp
            ),
            contentColor = if (tag == currentTag) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.height(40.dp)
    ) {
        TagButtonContent(tag)
    }
}

@Composable
fun TagButtonContent(tag: Tag) {
    Row {
        Column {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(tag.colour.toColor())
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = tag.title, fontSize = 15.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTag(viewModel: MainViewModel) {
    val addTagFocusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf("") }
    val (color, setColor) = remember { mutableStateOf(Color.Transparent) }
    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp), ButtonDefaults.shape)
            .clip(ButtonDefaults.shape)
            .height(40.dp)
            .width(200.dp)
    )
    {
        Column {
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add a tag...",
                    modifier = Modifier.noRippleClickable(onClick = { addTagFocusRequester.requestFocus() })
                )
                Spacer(modifier = Modifier.width(5.dp))
                Column {
                    Spacer(modifier = Modifier.height(2.dp))
                    chooseColor(color, setColor, 20.dp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                val interactionSource = remember { MutableInteractionSource() }
                val visualTransformation = VisualTransformation.None
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    interactionSource = interactionSource,
                    visualTransformation = visualTransformation,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    textStyle = TextStyle(fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key != Key.Enter) return@onKeyEvent false
                            if (keyEvent.type == KeyEventType.KeyUp) {
                                print(color)
                                print(color.toColour())
                                val tag = Tag(text, color.toColour())
                                viewModel.createTag(tag)
                                text = ""
                                setColor(Color.Transparent)
                            }
                            true
                        }
                        .focusRequester(addTagFocusRequester),
                    singleLine = true
                ) { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = text,
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
                        placeholder = {
                            Text(
                                "Add a tag...",
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        },
                        contentPadding = PaddingValues(0.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun displayTags(
    tagStatus: Map<Tag, Boolean>
) {
    val tagStatusList = tagStatus.toList().filter { it.second }
    tagStatusList.forEach {
        displayTag(it.first)
        if (it != tagStatusList.last()) {
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}

@Composable
fun displayTag(
    tag: Tag
) {
    Row (verticalAlignment = Alignment.CenterVertically){
        Column {
            Spacer(modifier = Modifier.height(9.dp))
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(tag.colour.toColor())
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = tag.title, fontSize = 10.sp)
        Spacer(modifier = Modifier.width(5.dp))
    }
}
