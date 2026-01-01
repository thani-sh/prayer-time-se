package me.thanish.prayers.se.routes.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.thanish.prayers.se.R
import me.thanish.prayers.se.domain.NotificationOffset

@Composable
fun SelectOffsetSlider(
    offset: NotificationOffset,
    onOffsetChange: (NotificationOffset) -> Unit
) {
    val context = LocalContext.current

    Column (
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.route_settings_notifications_before_adhan),
                fontSize = 12.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.weight(2f)
            )
            Text(
                text = offset.getLabel(context),
                fontSize = 12.sp,
                textAlign = TextAlign.Right,
            )
        }
        Slider(
            value = offset.offset.toFloat(),
            onValueChange = { onOffsetChange(NotificationOffset(it.toInt())) },
            valueRange = NotificationOffset.MIN_OFFSET.toFloat()..NotificationOffset.MAX_OFFSET.toFloat(),
        )
    }
}
