package me.thanish.prayers.se.routes.compass.components

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

/**
 * The frequency of location updates in milliseconds.
 */
private const val LOCATION_FREQUENCY = 1000 * 60 * 60

/**
 * The permission to request approximate location.
 */
private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION

/**
 * A composable that requests the current location of the user.
 */
@Composable
fun CurrentLocation(onLocationResult: (Location?) -> Unit) {
    val context = LocalContext.current
    val client = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Permission launcher
    val contract = ActivityResultContracts.RequestMultiplePermissions()
    val launcher = rememberLauncherForActivityResult(contract) { permissions ->
        when {
            // Approximate location granted. Request location updates.
            permissions.getOrDefault(LOCATION_PERMISSION, false) -> {
                requestLocationUpdates(client, onLocationResult)
            }

            // No location access granted. Maybe use approximate location?
            else -> {
                onLocationResult(null)
            }
        }
    }

    // Check and request permissions
    LaunchedEffect(key1 = true) {
        when {
            // Permissions already granted. Request location updates.
            hasLocationPermission(context) -> {
                requestLocationUpdates(client, onLocationResult)
            }

            // Show rationale and then request permissions
            // TODO: explain why we need location permission
            shouldShowRequestPermissionRationale(context) -> {
                launcher.launch(arrayOf(LOCATION_PERMISSION))
            }

            // Request permissions directly
            else -> {
                launcher.launch(arrayOf(LOCATION_PERMISSION))
            }
        }
    }
}

/**
 * Checks if the user has granted the given permission.
 */
private fun hasLocationPermission(context: Context): Boolean {
    return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
        context,
        LOCATION_PERMISSION
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

/**
 * Checks if the user should be shown a rationale for requesting the location permission.
 */
private fun shouldShowRequestPermissionRationale(context: Context): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(
        context as Activity,
        LOCATION_PERMISSION
    )
}
