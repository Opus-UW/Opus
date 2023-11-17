
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.viewmodel.viewModel
import ui.*
import ui.components.OpusTopAppBar
import viewmodels.MainViewModel


@Composable
fun App() {
    PreComposeApp {
        MaterialTheme {
            val viewModel = viewModel(modelClass = MainViewModel::class, keys = listOf("main")) {
                MainViewModel(it)
            }
            // Grab Data from server
            LaunchedEffect(Unit) {
                viewModel.fetchAllData()
            }
            val navigator = rememberNavigator()
            val coroutineScope = rememberCoroutineScope()

            // Data
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

            val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    NavigationContent(viewModel)
                },
                gesturesEnabled = true
            ) {
                Scaffold(
                    topBar = {
                        AnimatedVisibility(visible = currentScreen != "/login") {
                            OpusTopAppBar(viewModel, navigator) {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }
                        }
                    }
                ) {
                    Surface(modifier = Modifier.padding(top = it.calculateTopPadding())) {
                        NavHost(
                            navigator = navigator,
                            initialRoute = "/login"
                        ) {
                            scene(route = "/login") {
                                LoginScreen(viewModel, navigator)
                            }
                            scene(route = "/tasks") {
                                TaskScreen(viewModel)
                            }
                            scene(route = "/notes") {
                                NoteScreen(viewModel)
                            }
                            scene(route = "/calendar") {
                                CalendarScreen(viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}