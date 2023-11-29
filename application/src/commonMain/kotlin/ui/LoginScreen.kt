package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import api.ApiClient
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.oauth2.Oauth2
import com.google.api.services.oauth2.Oauth2Scopes
import com.google.api.services.tasks.TasksScopes
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.components.darkModeLogoVector
import ui.components.lightModeLogoVector
import ui.theme.isDarkTheme
import viewmodels.MainViewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader

private const val APPLICATION_NAME = "Opus"
private const val TOKENS_DIRECTORY_PATH = "tokens"
private const val CREDENTIALS_FILE_PATH = "/credentials.json"

private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

// If modifying these scopes, delete your previously saved tokens/ folder.
private val SCOPES = listOf(TasksScopes.TASKS) + Oauth2Scopes.all()
@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    navigator: Navigator
) {
    val (showLoginDialog, setShowLoginDialog) = remember { mutableStateOf(false) }
    if (showLoginDialog) {
        Dialog(onDismissRequest = { setShowLoginDialog(false) }) {
            Card(
                modifier = Modifier.height(250.dp).width(500.dp).padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    androidx.compose.material3.Text(
                        text = "Redirecting you to your browser..."
                    )
                }
            }
        }
    }

    val logoVector = if (isDarkTheme()) darkModeLogoVector() else lightModeLogoVector()
    Column{
        Spacer(modifier = Modifier.weight(1f))
        Row{
            Spacer(modifier = Modifier.weight(1f))
                Icon (logoVector, "Opus logo", tint = Color.Unspecified, modifier = Modifier.padding(vertical = 10.dp, horizontal = 30.dp))
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(1f))
        Row{
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                setShowLoginDialog(true)
                Login(viewModel, navigator)
            }){
                Icon(Icons.Default.Login, contentDescription = "Login Button",
                    tint = Color.White, modifier = Modifier.padding(5.dp))
                Text("LOGIN", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(5.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

object Cred {
    @Throws(IOException::class)
    fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential { // Load client secrets.
        val credentials = Cred::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH) ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")

        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(credentials))

        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build()

        val receiver = LocalServerReceiver.Builder().setPort(-1).build()
        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun Login(
    viewModel: MainViewModel,
    navigator: Navigator,
) {
    GlobalScope.launch {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val cred = Cred.getCredentials(httpTransport)
        viewModel.setCredential(cred)
        ApiClient.getInstance().setAccessToken(cred.accessToken)
        val oauth2 = Oauth2.Builder(httpTransport, JSON_FACTORY, cred).build()

        ApiClient.getInstance().setUserId(oauth2.userinfo().get().execute().id)

        viewModel.setUser(ApiClient.getInstance().getOrCreateUser())

        // Grab Data from server
        viewModel.fetchAllData()

        navigator.navigate("/tasks")
        viewModel.setCurrentScreen("/tasks")
    }
}