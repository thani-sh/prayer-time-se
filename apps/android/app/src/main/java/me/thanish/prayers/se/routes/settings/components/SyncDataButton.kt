package me.thanish.prayers.se.routes.settings.components

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.thanish.prayers.se.R
import me.thanish.prayers.se.domain.PrayerTimeRepository
import me.thanish.prayers.se.worker.SyncNotificationHelper

@Composable
fun SyncDataButton() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isSyncing by remember { mutableStateOf(false) }
    val successText = stringResource(R.string.route_settings_sync_data_success)
    val failureText = stringResource(R.string.route_settings_sync_data_failure)

    Button(
        onClick = {
            isSyncing = true
            scope.launch {
                try {
                    PrayerTimeRepository.syncIfNeeded(context) { current, total ->
                        SyncNotificationHelper.showProgress(context, current, total)
                    }
                    Toast.makeText(
                        context,
                        successText,
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        failureText,
                        Toast.LENGTH_SHORT
                    ).show()
                } finally {
                    isSyncing = false
                    SyncNotificationHelper.clear(context)
                }
            }
        },
        enabled = !isSyncing,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = stringResource(R.string.route_settings_sync_data))
    }
}
