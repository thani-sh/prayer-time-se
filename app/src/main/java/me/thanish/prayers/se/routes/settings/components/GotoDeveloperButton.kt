package me.thanish.prayers.se.routes.settings.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import me.thanish.prayers.se.R

@Composable
fun GotoDeveloperButton(
    onGotoDeveloper: () -> Unit,
) {
    TextButton(
        onClick = { onGotoDeveloper() },
        modifier = Modifier.alpha(0.5f)
    ) {
        Text(
            text = stringResource(R.string.route_developer_name),
            fontSize = 14.sp,
            letterSpacing = 0.5.sp,
        )
    }
}
