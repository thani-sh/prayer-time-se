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
import androidx.glance.unit.ColorProvider
import me.thanish.prayers.se.R
import me.thanish.prayers.se.domain.PrayerTime
import me.thanish.prayers.se.domain.PrayerTimeCity
import me.thanish.prayers.se.domain.PrayerTimeMethod
import me.thanish.prayers.se.domain.PrayerTimeTable

@Composable
fun WidgetContent(prayerTime: PrayerTime) {
    val context = LocalContext.current
    val dimmedColor = GlanceTheme.colors.secondary
        .getColor(LocalContext.current)
        .copy(alpha = 0.5f)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface)
            .padding(8.dp)
    ) {
        Spacer(GlanceModifier.defaultWeight())
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier.fillMaxWidth()
        ) {
            Text(
                text = prayerTime.type.getLabel(context),
                style = TextStyle(
                    fontSize = 15.sp,
                    color = GlanceTheme.colors.onSurface
                )
            )
            Text(
                text = prayerTime.getTimeString(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = GlanceTheme.colors.onSurface
                )
            )
            Text(
                text = prayerTime.getUntilString(context),
                style = TextStyle(
                    fontSize = 8.sp,
                    color = GlanceTheme.colors.onSurface
                ),
            )
            Spacer(GlanceModifier.height(8.dp))
            Text(
                text = "( ${context.getString(R.string.next_prayer_widget_refresh)} )",
                style = TextStyle(
                    fontSize = 8.sp,
                    color = ColorProvider(dimmedColor)
                ),
            )
        }
        Spacer(GlanceModifier.defaultWeight())
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
