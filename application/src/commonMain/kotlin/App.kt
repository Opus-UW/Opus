
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import api.ApiClient
import ui.theme.OpusTheme
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.viewmodel.viewModel
import ui.*
import ui.OpusTopAppBar
import ui.components.LoadingDialog
import ui.components.TagBar
import ui.components.getTheme
import ui.components.noRippleClickable
import ui.theme.isDarkTheme
import viewmodels.MainViewModel


@Composable
fun App() {
    PreComposeApp {
        val viewModel = viewModel(modelClass = MainViewModel::class, keys = listOf("main")) {
            MainViewModel(it)
        }
        val darkTheme by viewModel.darkTheme.collectAsStateWithLifecycle()
        val isDarkTheme = isDarkTheme()

        LaunchedEffect(Unit){
            viewModel.setDarkTheme(getTheme(isDarkTheme))
        }

        OpusTheme (useDarkTheme = darkTheme ?: true) {
            val navigator = rememberNavigator()
            val coroutineScope = rememberCoroutineScope()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
            val tags by viewModel.tags.collectAsStateWithLifecycle()
            val loading by viewModel.loading.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                coroutineScope.launch { ApiClient.getInstance().startClientConn { viewModel.fetchAllData() } }
            }
            val (showLoading, setShowLoading) = remember { mutableStateOf(false) }

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    NavigationContent(viewModel, navigator, drawerState)
                },
                gesturesEnabled = currentScreen != "/login"
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
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.noRippleClickable {  }
                ) {
                    Surface(modifier = Modifier.padding(top = it.calculateTopPadding())) {
                        Column {
                            if (currentScreen != "/login"){
                                TagBar(viewModel, tags)
                            }
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
                                scene(route = "/tags"){
                                    TagScreen(viewModel)
                                }
                            }
                        }
                        when {
                            loading == true ->{
                                LoadingDialog(viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}