package me.thanish.prayers.se.router

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(nav: NavController) {
    NavigationBar {
        routes.forEach { r ->
            val isSelected = getCurrentRoute(nav) == r.name
            val routeIcons = r.icon()

            NavigationBarItem(
                icon = {
                    Icon(
                        if (isSelected) routeIcons.first else routeIcons.second,
                        contentDescription = r.text
                    )
                },
                label = { Text(r.text) },
                selected = isSelected,
                onClick = { nav.navigate(r.name) }
            )
        }
    }
}

@Composable
fun getCurrentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
