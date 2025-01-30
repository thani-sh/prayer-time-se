package me.thanish.prayers.se.routes.compass.components

import android.hardware.SensorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotateRad
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.thanish.prayers.se.R


/**
 * A line in 2D space.
 */
data class Line(val p0: Offset, val p1: Offset, val len: Float)

/**
 * Size of the dashes in the compass.
 */
const val DASH_WIDTH = 4f

/**
 * A compass that shows the direction to north and the qibla.
 */
@Composable
fun QiblaCompass(qibla: Float, heading: Float, priority: Int) {
    val textMeasurer = rememberTextMeasurer()
    val linesColor = MaterialTheme.colorScheme.outlineVariant

    val priorityText = getPriorityText(priority)
    val priorityColor = getPriorityColor(priority)
    val priorityStyle =
        TextStyle(fontSize = 12.sp, textAlign = TextAlign.Center, color = priorityColor)
    val priorityLayout = remember(priorityText, priorityStyle) {
        textMeasurer.measure(priorityText, priorityStyle)
    }

    val textStyle = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center, color = linesColor)
    val textContent = stringResource(R.string.route_compass_north)
    val textLayout = remember(textContent, textStyle) {
        textMeasurer.measure(textContent, textStyle)
    }

    // Animate the heading for a smoother rotation
    val animatedHeading by animatedRotation(heading)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(x = size.width / 2f, y = size.height / 2f)
            val radius = size.minDimension / 2f
            val offset = -(Math.PI / 2f).toFloat()

            rotateRad(radians = animatedHeading, pivot = center) {
                val segmentSize = (radius * 2f * Math.PI / 60f - DASH_WIDTH).toFloat()
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(DASH_WIDTH, segmentSize))

                // draw segment lines
                drawCircle(
                    radius = radius,
                    color = linesColor,
                    style = Stroke(width = 16.dp.toPx(), pathEffect = pathEffect)
                )

                // draw north letter
                drawText(
                    textMeasurer = textMeasurer,
                    text = textContent,
                    style = textStyle,
                    topLeft = Offset(
                        x = center.x - textLayout.size.width / 2f,
                        y = center.y - textLayout.size.height / 2f - radius - 60f
                    )
                )
            }

            rotateRad(radians = animatedHeading + offset + qibla, pivot = center) {
                // draw qibla line
                val qiblaLine = Line(
                    p0 = center,
                    p1 = Offset(x = center.x + radius + 16, y = center.y),
                    len = radius
                )
                drawLine(
                    color = priorityColor,
                    start = qiblaLine.p0,
                    end = qiblaLine.p1,
                    cap = StrokeCap.Square,
                    strokeWidth = 3.dp.toPx()
                )
                drawLine(
                    color = priorityColor,
                    start = qiblaLine.p1,
                    end = qiblaLine.p1.plus(Offset(-32f, 20f)),
                    cap = StrokeCap.Square,
                    strokeWidth = 3.dp.toPx()
                )
                drawLine(
                    color = priorityColor,
                    start = qiblaLine.p1,
                    end = qiblaLine.p1.plus(Offset(-32f, -20f)),
                    cap = StrokeCap.Square,
                    strokeWidth = 3.dp.toPx()
                )
                drawRect(
                    color = Color.Black,
                    topLeft = qiblaLine.p1.plus(Offset(20f, -20f)),
                    size = Size(40f, 40f)
                )
                drawLine(
                    color = Color.Yellow,
                    start = qiblaLine.p1.plus(Offset(50f, -12f)),
                    end = qiblaLine.p1.plus(Offset(50f, 12f)),
                    cap = StrokeCap.Butt,
                    strokeWidth = 2.dp.toPx()
                )
            }

            drawText(
                textMeasurer = textMeasurer,
                text = priorityText,
                style = priorityStyle,
                topLeft = Offset(
                    x = center.x - priorityLayout.size.width / 2f,
                    y = center.y - priorityLayout.size.height / 2f + radius + 160f
                )
            )
        }
    }
}

/**
 * Returns text for the accuracy of data returned by sensors.
 */
@Composable
fun getPriorityText(priority: Int): String {
    if (priority >= SensorManager.SENSOR_STATUS_ACCURACY_HIGH) {
        return stringResource(R.string.route_compass_priority_high)
    }
    if (priority >= SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM) {
        return stringResource(R.string.route_compass_priority_medium)
    }
    if (priority >= SensorManager.SENSOR_STATUS_ACCURACY_LOW) {
        return stringResource(R.string.route_compass_priority_low)
    }
    return stringResource(R.string.route_compass_priority_error)
}

/**
 * Returns text for the accuracy of data returned by sensors.
 */
@Composable
fun getPriorityColor(priority: Int): Color {
    if (priority >= SensorManager.SENSOR_STATUS_ACCURACY_HIGH) {
        return MaterialTheme.colorScheme.primary
    }
    if (priority >= SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM) {
        return MaterialTheme.colorScheme.primary
    }
    if (priority >= SensorManager.SENSOR_STATUS_ACCURACY_LOW) {
        return MaterialTheme.colorScheme.outlineVariant
    }
    return MaterialTheme.colorScheme.error
}

/**
 * Animates the rotation of an angle handling wrap around.
 */
@Composable
fun animatedRotation(newValue: Float): State<Float> {
    val previous: Float by remember { mutableFloatStateOf(newValue) }

    return animateFloatAsState(
        targetValue = angleDifference(previous, newValue),
        animationSpec = tween(durationMillis = 200),
        label = "animated_rotation"
    )
}
