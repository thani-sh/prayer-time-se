package me.thanish.prayers.se.routes.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.thanish.prayers.se.times.PrayerTimes

val labelStyle = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 0.5.sp
)

val valueStyle = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal,
    letterSpacing = 0.5.sp
)

@Composable
fun MainRouteContent(times: PrayerTimes) {
    val items = listOf(
        Pair("Fajr", times.fajr.time.toLocalTime().toString()),
        Pair("Shuruk", times.shuruk.time.toLocalTime().toString()),
        Pair("Dhohr", times.dhohr.time.toLocalTime().toString()),
        Pair("Asr", times.asr.time.toLocalTime().toString()),
        Pair("Maghrib", times.maghrib.time.toLocalTime().toString()),
        Pair("Isha", times.isha.time.toLocalTime().toString())
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .width(180.dp)
                    .padding(vertical = 18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    textAlign = TextAlign.End,
                    style = labelStyle,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                )
                Text(
                    text = value,
                    textAlign = TextAlign.Start,
                    style = valueStyle,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                )
            }
            if (label != "Isha") {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .width(180.dp)
                        .alpha(0.1f),
                )
            }
        }
    }
}
