package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.models.opus.models.Tag
import viewmodels.MainViewModel

@Composable
fun TagBar(
    viewModel: MainViewModel,
    tags: List<Tag>
){
    val (showAllTags, setShowAllTags) = remember { mutableStateOf(false) }

    Column{
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {setShowAllTags(!showAllTags)}) {
                Icon(if (!showAllTags) Icons.Default.ExpandMore else Icons.Default.ExpandLess, contentDescription = "Expand Tag Bar")
            }
            AddTag(viewModel)
            Spacer(modifier = Modifier.width(5.dp))
            if (!showAllTags) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    items(
                        items = tags,
                        key = {
                            it.id
                        }) { tag ->
                        TagButton(viewModel, tag)
                    }
                }
            }
        }
        if (showAllTags){
            Spacer(modifier = Modifier.height(15.dp))
            LazyHorizontalStaggeredGrid(
                rows = StaggeredGridCells.Adaptive(35.dp),
                horizontalItemSpacing = 5.dp,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.2f),
                content = {
                    items(tags.size) { tag ->
                        TagButton(viewModel, tags[tag])
                    }
                }
            )
        }
    }
}