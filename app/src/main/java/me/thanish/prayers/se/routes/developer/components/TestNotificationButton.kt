package me.thanish.prayers.se.routes.developer.components

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
        onClick = { onTestNotification(60) },
    ) {
        Text(
            text = stringResource(R.string.route_developer_test_notification),
            fontSize = 14.sp,
            letterSpacing = 0.5.sp,
        )
    }
}
