package me.thanish.prayers.se.states

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun RequestPermission(
    requestedPermissions: Array<String>,
    handleSuccess: () -> Unit = {},
    handleFailure: () -> Unit = {}
) {
    val contract = ActivityResultContracts.RequestMultiplePermissions()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract) { permissions ->
        val hasAllPermissions = permissions.all { (permission: String) ->
            permissions.getOrDefault(permission, false)
        }

        when {
            hasAllPermissions -> {
                handleSuccess()
            }

            else -> {
                handleFailure()
            }
        }
    }

    // Check and request permissions
    LaunchedEffect(key1 = true) {
        val isAllPermissionGranted = requestedPermissions.all {
            isPermissionGranted(context, it)
        }

        when {
            isAllPermissionGranted -> {
                handleSuccess()
            }

            else -> {
                launcher.launch(requestedPermissions)
            }
        }
    }
}

/**
 * Checks if the user should be shown a rationale for requesting permission.
 */
fun shouldShowRationale(context: Context, permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(
        context as Activity,
        permission
    )
}

/**
 * Checks if a permission is granted.
 */
fun isPermissionGranted(context: Context, permission: String): Boolean {
    return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
        context,
        permission
    )
}
