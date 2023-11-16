package ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import org.models.opus.models.Task
import viewmodels.MainViewModel

@Composable
actual fun TaskCheckbox(
    viewModel: MainViewModel,
    task: Task?,
    new: Boolean,
    isTaskFocused: Boolean,
    textFieldFocusRequester: FocusRequester
) {
    IconButton(onClick = {
        // delete the task (note change to finished)
        if (task != null) {
            viewModel.updateTask(task = task, completed = !task.completed)
        }
        // For new task
        // Clicking on the plus focuses text box
        else {
            textFieldFocusRequester.requestFocus()
        }
    }) {
        Icon(
            // If create new task, plus and circle icon
            if (new) {
                if (isTaskFocused) Icons.Outlined.Circle else Icons.Default.Add
            } else {
                 Icons.Outlined.Circle
            },
            contentDescription = "Check mark",
        )
    }
}