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
import androidx.glance.layout.fillMaxSize
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
fun WidgetContent(prayerTime: PrayerTime) {
    val context = LocalContext.current

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = prayerTime.type.getLabel(context),
            style = TextStyle(
                fontSize = 14.sp,
                color = GlanceTheme.colors.onSurface
            )
        )
        Text(
            text = prayerTime.getTimeString(context),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = GlanceTheme.colors.onSurface
            )
        )
    }
}

@Composable
@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 80, heightDp = 100)
fun WidgetContentAsrPreview() {
    val times = PrayerTimeTable.forToday(LocalContext.current, PrayerTimeMethod.islamiskaforbundet, PrayerTimeCity.stockholm)

    GlanceTheme(GlanceTheme.colors) {
        WidgetContent(times.asr)
    }
}

@Composable
@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 80, heightDp = 100)
fun WidgetContentMaghribPreview() {
    val times = PrayerTimeTable.forToday(LocalContext.current, PrayerTimeMethod.islamiskaforbundet, PrayerTimeCity.stockholm)

    GlanceTheme(GlanceTheme.colors) {
        WidgetContent(times.maghrib)
    }
}
