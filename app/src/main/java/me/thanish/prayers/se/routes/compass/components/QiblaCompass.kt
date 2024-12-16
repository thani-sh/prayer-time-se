package me.thanish.prayers.se.routes.compass.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotateRad
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin


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
fun QiblaCompass(qibla: Float, heading: Float) {
    val linesColor = MaterialTheme.colorScheme.outlineVariant
    val northColor = MaterialTheme.colorScheme.error
    val qiblaColor = MaterialTheme.colorScheme.primary

    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(fontSize = 20.sp, color = northColor)
    val textContent = "N"
    val textLayout = remember(textContent, textStyle) {
        textMeasurer.measure(textContent, textStyle)
    }

    // Animate the heading for a smoother rotation
    val animatedHeading by animatedRotation(heading)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
        contentAlignment = Alignment.Center
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

                rotateRad(radians = offset, pivot = center) {
                    // draw qibla line
                    val qiblaLine = Line(
                        p0 = center,
                        p1 = Offset(
                            x = (center.x + radius * cos(qibla)),
                            y = (center.y + radius * sin(qibla)),
                        ),
                        len = radius
                    )
                    drawLine(
                        color = qiblaColor,
                        start = qiblaLine.p0,
                        end = qiblaLine.p1,
                        cap = StrokeCap.Round,
                        strokeWidth = 4.dp.toPx()
                    )
                }
            }
        }
    }
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
