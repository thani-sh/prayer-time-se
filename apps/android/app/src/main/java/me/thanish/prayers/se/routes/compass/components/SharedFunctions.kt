package me.thanish.prayers.se.routes.compass.components

import kotlin.math.PI

/**
 * Normalizes an angle to the range [0, 2*PI].
 */
fun normalizeAngle(angle: Float): Float {
    return ((angle + 2 * PI) % (2 * PI)).toFloat()
}

/**
 * Calculates the shortest difference between two angles.
 */
fun angleDifference(prev: Float, next: Float): Float {
    val diff = ((next - prev + 2 * PI) % (2 * PI)).toFloat()
    return if (diff > PI) prev - ((2 * PI).toFloat() - diff) else prev + diff
}
