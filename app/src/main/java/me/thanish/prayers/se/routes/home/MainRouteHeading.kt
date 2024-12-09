package me.thanish.prayers.se.routes.home


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MainRouteHeading() {
    Text(text = "Bönetider", style = MaterialTheme.typography.headlineLarge)
    Text(
        text = getDateString(),
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.alpha(0.75f)
    )
}

/**
 * get a formatted date string for the given date
 */
fun getDateString(date: LocalDate = LocalDate.now()): String {
    val svFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag("sv"))
    val arFormatter = DateTimeFormatter.ofPattern("MMM u هـ d", Locale.forLanguageTag("ar"))
    return "${date.format(svFormatter)}  |  ${HijrahDate.from(date).format(arFormatter)}"
}
