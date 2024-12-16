package me.thanish.prayers.se.routes.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import me.thanish.prayers.se.router.RouteSpec
import me.thanish.prayers.se.router.RouteType
import me.thanish.prayers.se.routes.settings.components.SettingsRouteContent
import me.thanish.prayers.se.states.getCity
import me.thanish.prayers.se.states.setCity
import me.thanish.prayers.se.ui.theme.PrayersTheme
import me.thanish.prayers.se.worker.SchedulerWorker

/**
 * Describes the route to use with navigation.
 */
val SettingsRouteSpec = RouteSpec(
    name = "settings",
    text = "Inställningar",
    type = RouteType.SECONDARY,
    icon = { Pair(Icons.Filled.Settings, Icons.Outlined.Settings) },
    content = { nav: NavController, modifier: Modifier -> SettingsRoute(nav, modifier) }
)

@Composable
fun SettingsRoute(nav: NavController, modifier: Modifier = Modifier) {
    var city by remember { mutableStateOf(getCity()) }

    val onCityChange = { selectedCity: String ->
        city = selectedCity
        setCity(selectedCity)
        SchedulerWorker.schedule(nav.context)
    }

    SettingsRouteView(city, onCityChange, modifier)
}

@Composable
fun SettingsRouteView(city: String, onCityChange: (String) -> Unit, modifier: Modifier = Modifier) {
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
                SettingsRouteContent(city, onCityChange)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun SettingsRoutePreview() {
    val city = "Uppsala"

    PrayersTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SettingsRouteView(city, {}, modifier = Modifier.padding(innerPadding))
        }
    }
}
