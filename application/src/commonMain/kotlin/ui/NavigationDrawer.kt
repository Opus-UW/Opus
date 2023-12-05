package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import ui.components.Logout
import ui.theme.isDarkTheme
import viewmodels.MainViewModel

private val SETTINGS_DIRECTORY_PATH = "${System.getProperty("user.home")}/.opus/settings"

@Composable
fun NavigationContent(
    viewModel: MainViewModel,
    navigator: Navigator,
    drawerState: DrawerState
) {
    val picture by viewModel.pictureURL.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val name by viewModel.userName.collectAsStateWithLifecycle()
    val darkTheme by viewModel.darkTheme.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    ModalDrawerSheet {
        Column(modifier = Modifier.padding(20.dp)) {
            picture?.let {
                Box(modifier = Modifier.size(100.dp).clip(CircleShape)) {
                    KamelImage(
                        resource = asyncPainterResource(data = it),
                        contentDescription = "pfp"
                    )
                }
            }
            name?.let { Text(it) }
            Spacer(modifier = Modifier.width(5.dp))
            email?.let { Text(it, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) }
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(Color.Transparent)
                    .clickable { viewModel.setDarkTheme(!darkTheme!!) }
            ) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.DarkMode, contentDescription = "Dark Mode")
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Dark Mode",
                            textAlign = TextAlign.Left
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = darkTheme == true,
                            onCheckedChange = { },
                            colors = SwitchDefaults.colors(
                                disabledCheckedBorderColor = MaterialTheme.colorScheme.primary,
                                disabledCheckedThumbColor = MaterialTheme.colorScheme.primaryContainer,
                                disabledCheckedIconColor = MaterialTheme.colorScheme.primaryContainer,
                                disabledCheckedTrackColor = MaterialTheme.colorScheme.primary,
                            ),
                            enabled = false
                        )

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(Color.Transparent)
                    .clickable {
                        coroutineScope.launch { drawerState.close() }
                        Logout(viewModel, navigator)
                    }
            ) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Icon(Icons.Default.Logout, contentDescription = "Dark Mode", tint = Color.Red)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Logout",
                            textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

