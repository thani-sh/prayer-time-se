package me.thanish.prayers.se.routes.compass


import android.hardware.SensorManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import me.thanish.prayers.se.R
import me.thanish.prayers.se.routes.RouteSpec
import me.thanish.prayers.se.routes.RouteType
import me.thanish.prayers.se.routes.compass.components.CurrentHeading
import me.thanish.prayers.se.routes.compass.components.CurrentLocation
import me.thanish.prayers.se.routes.compass.components.CurrentQibla
import me.thanish.prayers.se.routes.compass.components.QiblaCompass
import me.thanish.prayers.se.theme.PrayersTheme

/**
 * Describes the route to use with navigation.
 */
val CompassRouteSpec = RouteSpec(
    name = "compass",
    text = R.string.route_compass_name,
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
    var heading by remember { mutableFloatStateOf(0f) }
    var priority by remember { mutableIntStateOf(0) }
    var qibla by remember { mutableFloatStateOf(0f) }

    CurrentHeading(onHeadingChanged = { newHeading, newPriority ->
        heading = newHeading
        priority = newPriority
    })
    CurrentLocation(onLocationResult = { l -> qibla = CurrentQibla.getQiblaDirection(l) ?: 0f })

    CompassRouteView(qibla, heading, priority, modifier)
}

/**
 * The main route view without any states or event handlers.
 */
@Composable
fun CompassRouteView(
    qibla: Float,
    heading: Float,
    priority: Int,
    modifier: Modifier = Modifier
) {
    QiblaCompass(qibla, heading, priority)
}

/**
 * Preview of the main route with some example data.
 */
@Preview
@Composable
fun CompassRoutePreview() {
    PrayersTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            CompassRouteView(
                qibla = (Math.PI / 8f).toFloat(),
                heading = (Math.PI / 4f).toFloat(),
                priority = SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
