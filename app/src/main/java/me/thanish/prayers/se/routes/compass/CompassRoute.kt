package me.thanish.prayers.se.routes.compass


import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import me.thanish.prayers.se.R
import me.thanish.prayers.se.router.RouteSpec
import me.thanish.prayers.se.router.RouteType
import me.thanish.prayers.se.routes.compass.components.LockScreenOrientation
import me.thanish.prayers.se.routes.compass.components.QiblaCompass
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
    val context = LocalContext.current
    var azimuth by remember { mutableFloatStateOf(0f) }

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)

        val sensorEventListener = object : SensorEventListener {
            private var gravity: FloatArray? = null
            private var magnetic: FloatArray? = null

            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    gravity = event.values.clone()
                }
                if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    magnetic = event.values.clone()
                }
                if (gravity == null || magnetic == null) {
                    return
                }
                val success =
                    SensorManager.getRotationMatrix(rotationMatrix, null, gravity, magnetic)
                if (success) {
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    azimuth = ((Math.toDegrees(orientation[0].toDouble()) + 360) % 360f).toFloat()
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        }

        sensorManager.registerListener(
            sensorEventListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )
        sensorManager.registerListener(
            sensorEventListener,
            magnetometer,
            SensorManager.SENSOR_DELAY_GAME
        )

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    // TODO: calculate all these values
    val qibla = (Math.PI / 8f).toFloat()
    val heading = -Math.toRadians(azimuth.toDouble()).toFloat()
    CompassRouteView(qibla, heading, modifier)
}

/**
 * The main route view without any states or event handlers.
 */
@Composable
fun CompassRouteView(
    qibla: Float,
    heading: Float,
    modifier: Modifier = Modifier
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    QiblaCompass(qibla, heading)
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
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
