package ui.screens.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

//----------------------------------------------------------------------------------------
@Composable
fun SimpleTextTopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    TopAppBar {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = title
            )
        }
    }
}

//----------------------------------------------------------------------------------------
@Composable
fun GoBackTextTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back Button")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = title
            )
        }
    }
}