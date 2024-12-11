package me.thanish.prayers.se.routes.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import me.thanish.prayers.se.states.getCity
import me.thanish.prayers.se.states.setCity
import me.thanish.prayers.se.ui.theme.PrayersTheme

@Composable
fun SettingsRoute(nav: NavController, modifier: Modifier = Modifier) {
    var city by remember { mutableStateOf(getCity()) }

    val onClickDone = {
        println("navigating to main route")
        nav.navigate(route = "main")
    }

    val onCityChange = { selectedCity: String ->
        println("selected city: $selectedCity")
        city = selectedCity
        setCity(selectedCity)
    }

    SettingsRouteView(city, onCityChange, onClickDone, modifier)
}

@Composable
fun SettingsRouteView(city: String, onCityChange: (String) -> Unit, onClickDone: () -> Unit, modifier: Modifier = Modifier) {
    SettingsRouteLayout(
        primaryContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SettingsRouteContent(city, onCityChange)
            }
        },
        bottomContent = {
            SettingsRouteFooter(onClickDone)
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun SettingsRoutePreview() {
    val city = "Uppsala"

    PrayersTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SettingsRouteView(city, {}, {}, modifier = Modifier.padding(innerPadding))
        }
    }
}
