package me.thanish.prayers.se.routes.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSectionWithTitle(
    titleText: String,
    descriptionText: String?,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier
        .padding(top = 32.dp)
        .fillMaxWidth()) {
        Text(
            text = titleText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Surface(tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp, 16.dp)) {
                if (descriptionText != null) {
                    Text(
                        text = descriptionText,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                content()
            }
        }
    }
}
