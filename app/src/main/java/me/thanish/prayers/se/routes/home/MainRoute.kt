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
import androidx.navigation.NavController
import me.thanish.prayers.se.states.getCity
import me.thanish.prayers.se.times.PrayerTimes
import me.thanish.prayers.se.times.getPrayerTimesForDate
import me.thanish.prayers.se.ui.theme.PrayersTheme
import java.time.LocalDate

@Composable
fun MainRoute(nav: NavController, modifier: Modifier = Modifier) {
    val city by remember { mutableStateOf(getCity()) }
    val date by remember { mutableStateOf(LocalDate.now()) }
    val times = getPrayerTimesForDate(LocalContext.current, city, date)

    val onClickSelectDate = {
        // TODO: implement selecting date
        println("TODO: selecting date is not implemented")
    }

    val onClickPreferences = {
        println("navigating to settings route")
        nav.navigate(route = "settings")
    }

    MainRouteView(times, onClickSelectDate, onClickPreferences, modifier)
}

@Composable
fun MainRouteView(
    times: PrayerTimes,
    onClickSelectDate: () -> Unit,
    onClickPreferences: () -> Unit,
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
            MainRouteFooter(onClickSelectDate, onClickPreferences)
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
            MainRouteView(times, {}, {}, modifier = Modifier.padding(innerPadding))
        }
    }
}