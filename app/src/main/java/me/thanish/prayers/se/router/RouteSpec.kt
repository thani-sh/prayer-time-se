package me.thanish.prayers.se.router

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import me.thanish.prayers.se.routes.home.MainRouteSpec
import me.thanish.prayers.se.routes.settings.SettingsRouteSpec

/**
 * Describes the type of route.
 */
enum class RouteType {
    PRIMARY,  // Route is included in main navigation
    SECONDARY // Route is not included in main navigation
}

/**
 * Describes the structure of a route.
 */
data class RouteSpec(
    val name: String,
    val text: String = name,
    val type: RouteType = RouteType.PRIMARY,
    val icon: Pair<ImageVector, ImageVector> = Pair(Icons.Filled.Info, Icons.Outlined.Info),
    val content: @Composable (NavController, Modifier) -> Unit
)

/**
 * A list of all the routes in the app.
 */
val routes = listOf(MainRouteSpec, SettingsRouteSpec)

/**
 * The default route to use when navigating.
 */
val defaultRoute = MainRouteSpec
