package me.thanish.prayers.se.routes.settings

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import me.thanish.prayers.se.states.getCity
import me.thanish.prayers.se.ui.theme.PrayersTheme

@Composable
fun SettingsRoute(nav: NavController, modifier: Modifier = Modifier) {
    val city by remember { mutableStateOf(getCity()) }

    val onClickDone = {
        println("navigating to main route")
        nav.navigate(route = "main")
    }

    SettingsRouteView(city, onClickDone, modifier)
}

@Composable
fun SettingsRouteView(city: String, onClickDone: () -> Unit, modifier: Modifier = Modifier) {
    SettingsRouteLayout(
        primaryContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SettingsRouteContent(city)
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
            SettingsRouteView(city, {}, modifier = Modifier.padding(innerPadding))
        }
    }
}