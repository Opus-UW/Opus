package ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import org.models.opus.models.Task
import viewmodels.MainViewModel
import android.media.MediaPlayer
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun TaskCheckbox(
    viewModel: MainViewModel,
    task: Task?,
    new: Boolean,
    isTaskFocused: Boolean,
    textFieldFocusRequester: FocusRequester
) {
    val context = LocalContext.current
    val taskComplete by remember (task) { mutableStateOf(MediaPlayer.create(context, com.team202.opus.R.raw.complete)) }

    IconButton(onClick = {
        // delete the task (note change to finished)
        if (task != null) {
            val complete = !task.completed
            viewModel.updateTask(task = task, completed = complete)
            if (complete)
                taskComplete.start()
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