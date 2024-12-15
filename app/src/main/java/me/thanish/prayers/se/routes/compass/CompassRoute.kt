package me.thanish.prayers.se.routes.compass


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import me.thanish.prayers.se.R
import me.thanish.prayers.se.router.RouteSpec
import me.thanish.prayers.se.router.RouteType
import me.thanish.prayers.se.ui.theme.PrayersTheme

/**
 * Describes the route to use with navigation.
 */
val CompassRouteSpec = RouteSpec(
    name = "compass",
    text = "Kompass",
    type = RouteType.PRIMARY,
    icon = {
        Pair(
            ImageVector.vectorResource(R.drawable.baseline_explore_24),
            ImageVector.vectorResource(R.drawable.outline_explore_24),
        )
    },
    content = { nav: NavController, modifier: Modifier -> CompassRoute(nav, modifier) }
)

/**
 * The main route with all the states and event handlers.
 */
@Composable
fun CompassRoute(nav: NavController, modifier: Modifier = Modifier) {
    CompassRouteView(modifier)
}

/**
 * The main route view without any states or event handlers.
 */
@Composable
fun CompassRouteView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("COMPASS")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * Preview of the main route with some example data.
 */
@Preview
@Composable
fun CompassRoutePreview() {
    PrayersTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            CompassRouteView(modifier = Modifier.padding(innerPadding))
        }
    }
}
