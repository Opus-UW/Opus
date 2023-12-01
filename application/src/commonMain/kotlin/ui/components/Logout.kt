package ui.components

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.navigation.Navigator
import viewmodels.MainViewModel

expect fun Logout(
    viewModel: MainViewModel,
    navigator: Navigator
)