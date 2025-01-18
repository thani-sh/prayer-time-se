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
import me.thanish.prayers.se.domain.PrayerTimeTable
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Initial page date is the earliest date you can view.
 */
val INITIAL_PAGE_DATE: LocalDate = LocalDate.of(2024, 12, 1)

/**
 * Maximum page date is the latest date you can view.
 */
val MAXIMUM_PAGE_DATE: LocalDate = LocalDate.of(2025, 12, 31)

@Composable
fun MainRouteContent(
    city: PrayerTimeCity,
    initialDate: LocalDate,
    onDateChange: (LocalDate) -> Unit = {}
) {
    val context = LocalContext.current
    val initialPage = ChronoUnit.DAYS.between(INITIAL_PAGE_DATE, initialDate).toInt()
    val maximumPage = ChronoUnit.DAYS.between(INITIAL_PAGE_DATE, MAXIMUM_PAGE_DATE).toInt()
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { maximumPage })

    LaunchedEffect(pagerState.currentPage) {
        val scrolledPage = INITIAL_PAGE_DATE.plusDays(pagerState.currentPage.toLong())
        onDateChange(scrolledPage)
    }

    HorizontalPager(state = pagerState) { page ->
        val date = INITIAL_PAGE_DATE.plusDays(page.toLong())
        val times = PrayerTimeTable.forDate(LocalContext.current, city, date)

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
                    fontWeight = FontWeight.Bold,
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
                            .padding(horizontal = 12.dp),
                    )
                    Text(
                        text = prayerTime.getTimeString(),
                        textAlign = TextAlign.Start,
                        style = valueStyle,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
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
            .width(240.dp)
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
