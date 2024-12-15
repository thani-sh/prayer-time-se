package me.thanish.prayers.se.routes.settings.components


import androidx.compose.runtime.Composable


@Composable
fun SettingsRouteContent(city: String, onCityChange: (String) -> Unit) {
    SelectCityDropdown(city, onCityChange = onCityChange)
}
