package me.thanish.prayers.se.routes.compass.components

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import kotlin.math.PI

/**
 * A composable that requests the current heading of the device.
 */
@Composable
fun CurrentHeading(onHeadingChanged: (Float) -> Unit) {
    val context = LocalContext.current

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val rotation = FloatArray(9)
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
                    SensorManager.getRotationMatrix(rotation, null, gravity, magnetic)
                if (success) {
                    SensorManager.getOrientation(rotation, orientation)
                    onHeadingChanged(2 * PI.toFloat() - normalizeAngle(orientation[0]))
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, priority: Int) {}
        }

        sensorManager.registerListener(
            sensorEventListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
        sensorManager.registerListener(
            sensorEventListener,
            magnetometer,
            SensorManager.SENSOR_DELAY_UI
        )

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }
}
