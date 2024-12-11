package me.thanish.prayers.se.routes.settings


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun SettingsRouteContent(city: String) {
    Text(text = "SETTINGS")
    Text(text = "City = $city")
}
