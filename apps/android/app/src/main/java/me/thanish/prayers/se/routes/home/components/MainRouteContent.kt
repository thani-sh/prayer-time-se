package me.thanish.prayers.se.routes.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.thanish.prayers.se.domain.PrayerTimeCity
import me.thanish.prayers.se.domain.PrayerTimeMethod
import me.thanish.prayers.se.domain.PrayerTimeTable
import java.time.LocalDate
import java.time.temporal.ChronoUnit



@Composable
fun MainRouteContent(
    method: PrayerTimeMethod,
    city: PrayerTimeCity,
    initialDate: LocalDate,
    onDateChange: (LocalDate) -> Unit = {}
) {
    val context = LocalContext.current
    val today = remember { LocalDate.now() }
    val start = remember(today) { today.minusDays(7) }
    val count = 35

    val initialPage = remember(start, initialDate) {
        ChronoUnit.DAYS.between(start, initialDate).toInt()
    }

    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { count })

    LaunchedEffect(pagerState.currentPage) {
        val scrolledPage = start.plusDays(pagerState.currentPage.toLong())
        onDateChange(scrolledPage)
    }

    HorizontalPager(state = pagerState) { page ->
        val date = start.plusDays(page.toLong())
        val times = PrayerTimeTable.forDate(LocalContext.current, method, city, date)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            times.toList().forEachIndexed { index, prayerTime ->
                val textColor = if (prayerTime.isCurrentPrayer()) {
                    MaterialTheme.colorScheme.primary
                } else if (prayerTime.isNextPrayer(context)) {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                }
                val labelStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp,
                    color = textColor
                )
                val valueStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.5.sp,
                    color = textColor
                )

                if (index != 0) {
                    FadingHorizontalDivider()
                }

                Row(
                    modifier = Modifier.padding(vertical = 18.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = prayerTime.type.getLabel(context),
                        textAlign = TextAlign.End,
                        style = labelStyle,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                    )
                    Text(
                        text = prayerTime.getTimeString(context),
                        textAlign = TextAlign.Start,
                        style = valueStyle,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun FadingHorizontalDivider() {
    Box(
        modifier = Modifier
            .width(320.dp)
            .height(1.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0f),
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0f)
                    )
                )
            )
    )
}
