package me.thanish.prayers.se.routes.home


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import me.thanish.prayers.se.router.RouteSpec
import me.thanish.prayers.se.router.RouteType
import me.thanish.prayers.se.routes.home.components.MainRouteContent
import me.thanish.prayers.se.routes.home.components.MainRouteHeading
import me.thanish.prayers.se.states.getCity
import me.thanish.prayers.se.times.PrayerTimes
import me.thanish.prayers.se.times.getPrayerTimesForDate
import me.thanish.prayers.se.ui.theme.PrayersTheme
import java.time.LocalDate

/**
 * Describes the route to use with navigation.
 */
val MainRouteSpec = RouteSpec(
    name = "main",
    text = "Timetable",
    type = RouteType.PRIMARY,
    icon = Pair(Icons.Filled.DateRange, Icons.Outlined.DateRange),
    content = { nav: NavController, modifier: Modifier -> MainRoute(nav, modifier) }
)

/**
 * The main route with all the states and event handlers.
 */
@Composable
fun MainRoute(nav: NavController, modifier: Modifier = Modifier) {
    val city by remember { mutableStateOf(getCity()) }
    val date by remember { mutableStateOf(LocalDate.now()) }
    val times = getPrayerTimesForDate(LocalContext.current, city, date)

    MainRouteView(times, modifier)
}

/**
 * The main route view without any states or event handlers.
 */
@Composable
fun MainRouteView(
    times: PrayerTimes,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MainRouteHeading()
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MainRouteContent(times)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * Preview of the main route with some example data.
 */
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
