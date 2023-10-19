package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun CalendarScreen(toggleMenu: () -> Unit, showMenu: Boolean){
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { toggleMenu() }, enabled = !showMenu) {
            Icon(if (showMenu) Icons.Default.CalendarMonth else Icons.Default.Menu, contentDescription = "Menu Button")
        }
        Text("Calendar")
    }
}