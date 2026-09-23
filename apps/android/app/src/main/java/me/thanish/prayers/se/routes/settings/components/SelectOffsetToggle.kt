package me.thanish.prayers.se.routes.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import me.thanish.prayers.se.R
import me.thanish.prayers.se.domain.NotificationOffset

@Composable
fun SelectOffsetToggle(
    offset: NotificationOffset,
    onOffsetChange: (NotificationOffset) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.notification_before_adhan_enabled),
                fontSize = 12.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.weight(2f)
            )
            Switch(
                modifier = Modifier.scale(0.6f),
                checked = offset.isEnabled(),
                onCheckedChange = {
                    if (it) {
                        onOffsetChange(NotificationOffset(NotificationOffset.DEFAULT_OFFSET))
                    } else {
                        onOffsetChange(NotificationOffset(NotificationOffset.DISABLED_OFFSET))
                    }
                }
            )
        }
    }
}
