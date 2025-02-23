package me.thanish.prayers.se.routes.settings.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.thanish.prayers.se.R

@Composable
fun FeedbackIconButtons() {
    val context = LocalContext.current

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        IconAndTextButton(
            icon = ImageVector.vectorResource(R.drawable.baseline_code_24),
            text = stringResource(R.string.route_settings_open_github),
            onClick = { openUrl("https://github.com/thani-sh/prayer-time-se", context) }
        )
        IconAndTextButton(
            icon = Icons.Filled.Info,
            text = stringResource(R.string.route_settings_open_website),
            onClick = { openUrl("https://xn--bnetider-n4a.nu", context) }
        )
        IconAndTextButton(
            icon = Icons.Filled.Info,
            text = stringResource(R.string.route_settings_read_privacy),
            onClick = { openUrl("https://xn--bnetider-n4a.nu/docs/privacy", context) }
        )
    }
}

@Composable
fun IconAndTextButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {},
) {
    TextButton(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = text, modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

/**
 * openUrl opens a link on the web browser
 */
fun openUrl(url: String, context: Context) {
    val url = "https://prayertime.lk"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
