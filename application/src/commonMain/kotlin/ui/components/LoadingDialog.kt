package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import viewmodels.MainViewModel
import java.awt.Cursor

@Composable
fun LoadingDialog(
    viewModel: MainViewModel, snackbarState: SnackbarHostState
) {
    val authLink by viewModel.authLink.collectAsStateWithLifecycle()
    Dialog(onDismissRequest = { viewModel.setLoading(false) }) {
        Card(
            modifier = Modifier.fillMaxHeight(0.5f).fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp).padding(0.dp, 16.dp, 0.dp, 0.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    text = "Loading...", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface
                )
                authLink?.let { link ->
                    Text(
                        "Automatic sign in not working?", modifier = Modifier.padding(16.dp, 32.dp, 16.dp, 8.dp)
                    )
                    val clipboardManager = LocalClipboardManager.current
                    val scope = rememberCoroutineScope()

                    var currJob: Job? = null

                    TextButton(onClick = {
                        clipboardManager.setText(AnnotatedString(link))
                        currJob?.cancel() // Cancel current job if snackbar already showing
                        currJob = scope.launch {
                            snackbarState.showSnackbar("Copied!")
                        } // Start new job
                        currJob?.invokeOnCompletion { currJob = null } // Set currJob to null when done

                    }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copy to clipboard",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Copy to Clipboard")
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}