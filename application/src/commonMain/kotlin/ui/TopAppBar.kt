package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import utils.minusMonth
import utils.plusMonth
import viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpusTopAppBar(
    viewModel: MainViewModel,
    navigator: Navigator,
    toggleDrawer: () -> Unit
) {
    val curDate by viewModel.curDate.collectAsStateWithLifecycle()

    var taskState by remember { mutableStateOf(true) }
    var noteState by remember { mutableStateOf(false) }
    var calendarState by remember { mutableStateOf(false) }
    var tagState by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (taskState) "Tasks" else (if (noteState) "Notes" else  (if (calendarState) "Calendar" else (if (tagState) "Tags" else "Unknown")) ) )
                if (calendarState) {
                    Row(
                        Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.setCurDate(curDate.minusMonth(1)) },
                            enabled = true
                        ) { Icon(Icons.Default.ArrowBack, contentDescription = "Calender Left") }
                        Text(curDate.month.name + " " + curDate.year)
                        IconButton(
                            onClick = { viewModel.setCurDate(curDate.plusMonth(1)) },
                            enabled = true
                        ) { Icon(Icons.Default.ArrowForward, contentDescription = "Calender Right") }
                    }
                } else if (noteState || taskState){
                    Spacer(modifier = Modifier.weight(1f))
                    var text by remember { mutableStateOf("") }
                    val interactionSource = remember { MutableInteractionSource() }
                    val visualTransformation = VisualTransformation.None
                    val focusRequester = remember { FocusRequester() }
                    val focusManager = LocalFocusManager.current
                    Box(modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .clip(RoundedCornerShape(7.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(7.dp))
                        .padding(7.dp)
                    ){
                        Row {
                            BasicTextField(
                                value = text,
                                onValueChange = { text = it; viewModel.setSearchString(text) },
                                interactionSource = interactionSource,
                                visualTransformation = visualTransformation,
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                textStyle = TextStyle(fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface),
                                singleLine = true,
                                modifier = Modifier.weight(1f).focusRequester(focusRequester)
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
                                            "Search...",
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        )
                                    },
                                    contentPadding = PaddingValues(0.dp)
                                )
                            }
                            if (text != ""){
                                Icon(Icons.Default.Close, contentDescription = "Clear search", modifier = Modifier.clickable{text = ""; viewModel.setSearchString(text); focusManager.clearFocus()})
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                IconButton(
                    onClick = {
                        if (!taskState) {
                            tagState = false
                            taskState = true
                            noteState = false
                            calendarState = false
                            viewModel.setCurrentScreen("/tasks")
                            navigator.navigate("/tasks")
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.ViewList,
                        tint = if (taskState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Navigate to tasks"
                    )
                }
                IconButton(
                    onClick = {
                        if (!noteState) {
                            tagState = false
                            taskState = false
                            noteState = true
                            calendarState = false
                            viewModel.setCurrentScreen("/notes")
                            navigator.navigate("/notes")
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.StickyNote2,
                        tint = if (noteState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Navigate to notes"
                    )
                }
                IconButton(
                    onClick = {
                        if (!calendarState) {
                            tagState = false
                            taskState = false
                            noteState = false
                            calendarState = true
                            viewModel.setCurDate(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
                            viewModel.setCurrentScreen("/calendar")
                            navigator.navigate("/calendar")
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        tint = if (calendarState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Navigate to calendar"
                    )
                }
                IconButton(
                    onClick = {
                        if (!tagState) {
                            tagState = true
                            taskState = false
                            noteState = false
                            calendarState = false
                            viewModel.setCurrentScreen("/tags")
                            navigator.navigate("/tags")
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.Sell,
                        tint = if (tagState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Navigate to tags"
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = toggleDrawer) {
                Icon(Icons.Default.Menu, contentDescription = "Menu Button")
            }
        },

        )


}