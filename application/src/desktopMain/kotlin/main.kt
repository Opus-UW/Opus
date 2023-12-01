
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.File

fun main() = application {
    Window(
        onCloseRequest = {
            val TOKENS_DIRECTORY_PATH = "${System.getProperty("user.home")}/.opus/tokens"
            val tokenFile = File(TOKENS_DIRECTORY_PATH)
            tokenFile.deleteRecursively()
            exitApplication()
        },
        title = "Opus",
        ) {
        App()
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}