package me.thanish.prayers.se.theme.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import me.thanish.prayers.se.routes.routes

@Composable
fun BottomBar(nav: NavController) {
    NavigationBar {
        routes.forEach { r ->
            val isCurrent = getCurrentRoute(nav) == r.name
            val routeText = r.getLabel(LocalContext.current)
            val routeIcon = r.icon().let { routeIcons ->
                if (isCurrent) routeIcons.first else routeIcons.second
            }

            NavigationBarItem(
                icon = { Icon(routeIcon, contentDescription = routeText) },
                label = { Text(routeText) },
                selected = isCurrent,
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
