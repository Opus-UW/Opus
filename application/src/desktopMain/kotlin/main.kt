
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import java.awt.Dimension
import java.io.File

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = "Opus",
        icon = painterResource("logo.png")
        ) {
        window.minimumSize = Dimension(800, 600)
        App()
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}