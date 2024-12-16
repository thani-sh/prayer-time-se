package me.thanish.prayers.se.routes.compass.components

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import me.thanish.prayers.se.states.RequestPermission

/**
 * The frequency of location updates in milliseconds.
 */
private const val LOCATION_FREQUENCY = 1000 * 60 * 60

/**
 * A composable that requests the current location of the user.
 */
@Composable
fun CurrentLocation(onLocationResult: (Location?) -> Unit) {
    val context = LocalContext.current
    val client = remember { LocationServices.getFusedLocationProviderClient(context) }

    RequestPermission(
        requestedPermissions = arrayOf(ACCESS_COARSE_LOCATION),
        handleSuccess = { requestLocationUpdates(client, onLocationResult) },
        handleFailure = { onLocationResult(null) }
    )
}

/**
 * Requests location updates from the FusedLocationProviderClient.
 */
@SuppressLint("MissingPermission")
private fun requestLocationUpdates(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationResult: (Location?) -> Unit
) {
    val locationRequest = LocationRequest
        .Builder(Priority.PRIORITY_LOW_POWER, LOCATION_FREQUENCY.toLong())
        .build()
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            onLocationResult(locationResult.lastLocation)
        }
    }
    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )
}
