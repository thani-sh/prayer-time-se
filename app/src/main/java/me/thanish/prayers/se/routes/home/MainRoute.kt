package me.thanish.prayers.se.routes.home


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import me.thanish.prayers.se.states.getCity
import me.thanish.prayers.se.times.PrayerTimes
import me.thanish.prayers.se.times.getPrayerTimesForDate
import me.thanish.prayers.se.ui.theme.PrayersTheme
import java.time.LocalDate

@Composable
fun MainRoute(
    modifier: Modifier = Modifier
) {
    val city by remember { mutableStateOf(getCity()) }
    val date by remember { mutableStateOf(LocalDate.now()) }
    val times = getPrayerTimesForDate(LocalContext.current, city, date)
    MainRouteView(times, modifier)
}

@Composable
fun MainRouteView(
    times: PrayerTimes,
    modifier: Modifier = Modifier
) {
    MainRouteLayout(
        headerContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MainRouteHeading()
            }
        },
        primaryContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MainRouteContent(times)
            }
        },
        bottomContent = {
            MainRouteFooter()
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun MainRoutePreview() {
    val city = "Uppsala"
    val date = LocalDate.now()
    val times = getPrayerTimesForDate(LocalContext.current, city, date)

    PrayersTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MainRouteView(times, modifier = Modifier.padding(innerPadding))
        }
    }
}