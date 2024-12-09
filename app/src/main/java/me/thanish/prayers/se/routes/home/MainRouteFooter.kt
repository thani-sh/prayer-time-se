package me.thanish.prayers.se.routes.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainRouteFooter() {
    IconButton(onClick = {}) {
        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select date")
    }
    Spacer(modifier = Modifier.width(8.dp))
    IconButton(onClick = {}) {
        Icon(imageVector = Icons.Default.Settings, contentDescription = "Preferences")
    }
}