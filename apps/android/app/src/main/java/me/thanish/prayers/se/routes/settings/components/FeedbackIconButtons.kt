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
import androidx.compose.material.icons.filled.Email
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
            onClick = { openGitHubRepo(context) }
        )
        IconAndTextButton(
            icon = Icons.Filled.Email,
            text = stringResource(R.string.route_settings_report_errors),
            onClick = { openEmailComposer(context) }
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
 * openGithubRepo opens the GitHub repository in a browser
 */
fun openGitHubRepo(context: Context) {
    val url = "https://github.com/thani-sh/prayer-time-se-app"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

/**
 * openEmailComposer opens the email composer with the author's email address
 */
fun openEmailComposer(context: Context) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:mnmtanish@gmail.com")
        putExtra(Intent.EXTRA_SUBJECT, "Prayers app feedback")
        putExtra(Intent.EXTRA_TEXT, "Enter your message here...")
    }
    context.startActivity(Intent.createChooser(emailIntent, "Send email"))
}
