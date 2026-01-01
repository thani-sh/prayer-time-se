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
import me.thanish.prayers.se.domain.TimeFormat

@Composable
fun SelectTimeFormatToggle(
    format: TimeFormat,
    onFormatChange: (TimeFormat) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.route_settings_time_format),
                fontSize = 12.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.weight(2f)
            )
            Switch(
                modifier = Modifier.scale(0.6f),
                checked = format == TimeFormat.h24,
                onCheckedChange = {
                    if (it) {
                        onFormatChange(TimeFormat.h24)
                    } else {
                        onFormatChange(TimeFormat.h12)
                    }
                }
            )
        }
    }
}
