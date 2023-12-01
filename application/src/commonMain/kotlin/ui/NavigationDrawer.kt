package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.models.opus.models.Tag
import viewmodels.MainViewModel

@Composable
fun NavigationContent(
    viewModel: MainViewModel
) {
    val tags by viewModel.tags.collectAsStateWithLifecycle()

    ModalDrawerSheet {
        Text("General", modifier = Modifier.padding(16.dp))
        Divider(color = Color.Black, thickness = 1.dp)
        Text("Tags", modifier = Modifier.padding(16.dp))
    }
}

