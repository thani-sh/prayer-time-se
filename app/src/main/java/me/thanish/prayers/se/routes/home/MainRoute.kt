package me.thanish.prayers.se.routes.home


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import me.thanish.prayers.se.R
import me.thanish.prayers.se.domain.PrayerTimeCity
import me.thanish.prayers.se.routes.RouteSpec
import me.thanish.prayers.se.routes.RouteType
import me.thanish.prayers.se.routes.home.components.MainRouteContent
import me.thanish.prayers.se.routes.home.components.MainRouteHeading
import me.thanish.prayers.se.theme.PrayersTheme
import java.time.LocalDate

/**
 * Describes the route to use with navigation.
 */
val MainRouteSpec = RouteSpec(
    name = "main",
    text = R.string.route_home_name,
    type = RouteType.PRIMARY,
    icon = { Pair(Icons.Filled.DateRange, Icons.Outlined.DateRange) },
    content = { nav: NavController, modifier: Modifier -> MainRoute(nav, modifier) }
)

/**
 * The main route with all the states and event handlers.
 */
@Composable
fun MainRoute(nav: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val city by remember { mutableStateOf(PrayerTimeCity.get(context)) }
    var date by remember { mutableStateOf(LocalDate.now()) }

    val onDateChange = { selectedDate: LocalDate ->
        date = selectedDate
    }

    MainRouteView(city, date, onDateChange, modifier)
}

/**
 * The main route view without any states or event handlers.
 */
@Composable
fun MainRouteView(
    city: PrayerTimeCity,
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MainRouteHeading(date)
            Spacer(modifier = Modifier.height(48.dp))
            MainRouteContent(city, date, onDateChange)
        }
    }
}

/**
 * Preview of the main route with some example data.
 */
@Preview
@Composable
fun MainRoutePreview() {
    val city = PrayerTimeCity.stockholm
    val date = LocalDate.now()

    PrayersTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MainRouteView(city, date, {}, modifier = Modifier.padding(innerPadding))
        }
    }
}
