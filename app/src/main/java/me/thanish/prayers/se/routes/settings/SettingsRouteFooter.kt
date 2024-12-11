package me.thanish.prayers.se.routes.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun SettingsRouteFooter(
    onClickDone: () -> Unit,
) {
    IconButton(onClick = onClickDone) {
        Icon(imageVector = Icons.Default.Done, contentDescription = "Done")
    }
}