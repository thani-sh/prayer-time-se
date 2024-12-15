package me.thanish.prayers.se

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.thanish.prayers.se.router.BottomBar
import me.thanish.prayers.se.router.defaultRoute
import me.thanish.prayers.se.router.routes

@Composable
fun Navigation() {
    val nav = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar(nav) },
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        NavHost(navController = nav, startDestination = defaultRoute.name) {
            routes.forEach() { r -> composable(route = r.name) { r.content(nav, modifier) } }
        }
    }
}
