
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import api.ApiClient
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.viewmodel.viewModel
import ui.*
import ui.theme.OpusTheme
import viewmodels.MainViewModel


@Composable
fun App() {
    PreComposeApp {
        OpusTheme  {
            val viewModel = viewModel(modelClass = MainViewModel::class, keys = listOf("main")) {
                MainViewModel(it)
            }

            val navigator = rememberNavigator()
            val coroutineScope = rememberCoroutineScope()

            // Data
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

            val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

            coroutineScope.launch { ApiClient.getInstance().startClientConn{ viewModel.fetchAllData() } }

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
                    },
                    containerColor = MaterialTheme.colorScheme.onBackground
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