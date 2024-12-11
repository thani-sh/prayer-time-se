package me.thanish.prayers.se

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.thanish.prayers.se.routes.home.MainRoute
import me.thanish.prayers.se.routes.settings.SettingsRoute

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "main" ) {
        composable(route = "main") { MainRoute(nav = nav, modifier = modifier) }
        composable(route = "settings") { SettingsRoute(nav = nav, modifier = modifier) }
    }
}