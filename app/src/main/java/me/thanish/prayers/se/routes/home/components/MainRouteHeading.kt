package me.thanish.prayers.se.routes.home.components


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import me.thanish.prayers.se.R
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MainRouteHeading(date: LocalDate) {
    Text(
        text = stringResource(R.string.route_home_title),
        style = MaterialTheme.typography.headlineLarge
    )
    Text(
        text = "${getDefaultDateString(date)}  |  ${getHijrahDateString(date)}",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.alpha(0.75f)
    )
}

/**
 * getDefaultDateString returns the date string with current locale
 */
fun getDefaultDateString(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    return date.format(formatter)
}

/**
 * getHijrahDateString returns the hijrah date string with current locale
 */
fun getHijrahDateString(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("MMM u هـ d", Locale.forLanguageTag("ar"))
    return HijrahDate.from(date).format(formatter)
}
