package me.thanish.prayers.se.widget.nextprayer

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import me.thanish.prayers.se.domain.PrayerTime
import me.thanish.prayers.se.domain.PrayerTimeCity
import me.thanish.prayers.se.domain.PrayerTimeMethod
import me.thanish.prayers.se.domain.PrayerTimeTable

@Composable
fun WidgetContent(prayerTime: PrayerTime, modifier: GlanceModifier = GlanceModifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface)
            .padding(12.dp)
    ) {
        Spacer(GlanceModifier.defaultWeight())
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier.fillMaxWidth()
        ) {
            Text(
                text = prayerTime.type.getLabel(context).uppercase(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = GlanceTheme.colors.primary
                )
            )
        }
        Spacer(GlanceModifier.height(2.dp))
        Text(
            text = prayerTime.getTimeString(context),
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.onSurface
            )
        )
        Spacer(GlanceModifier.height(2.dp))
        Text(
            text = prayerTime.getUntilString(context),
            style = TextStyle(
                fontSize = 10.sp,
                color = GlanceTheme.colors.onSurfaceVariant
            )
        )
        Spacer(GlanceModifier.defaultWeight())
    }
}

@Composable
@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 170, heightDp = 80)
fun WidgetContentPreview() {
    val times = PrayerTimeTable.forToday(LocalContext.current, PrayerTimeMethod.islamiskaforbundet, PrayerTimeCity.stockholm)

    GlanceTheme(GlanceTheme.colors) {
        WidgetContent(times.asr)
    }
}
