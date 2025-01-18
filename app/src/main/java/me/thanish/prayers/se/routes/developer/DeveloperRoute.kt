package me.thanish.prayers.se.routes.developer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import me.thanish.prayers.se.R
import me.thanish.prayers.se.routes.RouteSpec
import me.thanish.prayers.se.routes.RouteType
import me.thanish.prayers.se.routes.developer.components.TestNotificationButton
import me.thanish.prayers.se.theme.PrayersTheme
import me.thanish.prayers.se.worker.NotificationWorker

/**
 * Describes the route to use with navigation.
 */
val DeveloperRouteSpec = RouteSpec(
    name = "developer",
    text = R.string.route_developer_name,
    type = RouteType.SECONDARY,
    icon = { Pair(Icons.Filled.Build, Icons.Outlined.Build) },
    content = { nav: NavController, modifier: Modifier -> DeveloperRoute(nav, modifier) }
)

@Composable
fun DeveloperRoute(nav: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val onTestNotification = { delay: Long ->
        NotificationWorker.scheduleTestNotification(context, delay)
    }

    DeveloperRouteView(
        onTestNotification,
        modifier
    )
}

@Composable
fun DeveloperRouteView(
    onTestNotification: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TestNotificationButton(onTestNotification)
        }
    }
}

@Preview
@Composable
fun DeveloperRoutePreview() {
    val onTestNotification: (Long) -> Unit = {}

    PrayersTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DeveloperRouteView(
                onTestNotification,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
