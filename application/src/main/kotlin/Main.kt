import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.datetime.LocalDateTime
import org.opus.models.Todo


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun task(task: Todo) {
    var options by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var done by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .onFocusChanged {
            options = it.isFocused
        }) {
        Box() {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    var hovered by remember { mutableStateOf(false) }
                    Icon( if (hovered) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                        contentDescription = "Check mark",
                        modifier = Modifier
                            .onPointerEvent(PointerEventType.Enter) {
                                hovered = true
                            }
                            .onPointerEvent(PointerEventType.Exit) {
                                hovered = false
                            }
                    )
                    TextField(
                        value = text,
                        placeholder = { Text("Add a task") },
                        onValueChange = { text = it },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Gray,
                            disabledTextColor = Color.Transparent,
                            backgroundColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
        Box() {
            if (options) {
                TextButton(onClick = {}) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar")
                }
            }
        }
    }

}


fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Opus") {
        val tasks: Todo = Todo(true, "nou", LocalDateTime(2002, 12, 21, 0, 0))
        Column {
            task(tasks)
            task(tasks)
        }
    }
}
