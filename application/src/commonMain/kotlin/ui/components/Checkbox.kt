package ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import org.models.opus.models.Task
import viewmodels.MainViewModel

@Composable
expect fun TaskCheckbox(
    viewModel: MainViewModel,
    task: Task?,
    new: Boolean,
    isTaskFocused: Boolean,
    textFieldFocusRequester: FocusRequester
)