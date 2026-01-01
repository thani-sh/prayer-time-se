package me.thanish.prayers.se.routes.settings.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import me.thanish.prayers.se.R


@Composable
fun TestNotificationButton(
    onTestNotification: (Long) -> Unit,
) {
    TextButton(
        onClick = { onTestNotification(1) },
    ) {
        Text(
            text = stringResource(R.string.route_settings_test_notification),
            fontSize = 12.sp,
            letterSpacing = 0.5.sp,
        )
    }
}
