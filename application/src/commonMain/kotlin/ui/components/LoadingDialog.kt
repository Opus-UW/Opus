package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import viewmodels.MainViewModel
import java.awt.Cursor

@Composable
fun LoadingDialog(
   viewModel: MainViewModel
){
    val authLink by viewModel.authLink.collectAsStateWithLifecycle()
    Dialog (onDismissRequest = { viewModel.setLoading(false)}){
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
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    text = "Loading...",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                authLink?.let {link ->
                    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
                        val str = "Click here to manually sign in"
                        val startIndex = str.indexOf("here")
                        val endIndex = startIndex + 4
                        append(str)
                        addStyle(
                            style = SpanStyle(
                                color = Color(0xff64B5F6),
                                fontSize = 16.sp,
                                textDecoration = TextDecoration.Underline
                            ), start = startIndex, end = endIndex
                        )

                        // attach a string annotation that stores a URL to the text "link"
                        addStringAnnotation(
                            tag = "URL",
                            annotation = link,
                            start = startIndex,
                            end = endIndex
                        )

                    }

                    val uriHandler = LocalUriHandler.current

                    ClickableText(modifier = Modifier
                        .padding(16.dp),
                        text = annotatedLinkString,
                        onClick = {
                            annotatedLinkString
                                .getStringAnnotations("URL", it, it)
                                .firstOrNull()?.let { stringAnnotation ->
                                    uriHandler.openUri(stringAnnotation.item)
                                }
                        },
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}