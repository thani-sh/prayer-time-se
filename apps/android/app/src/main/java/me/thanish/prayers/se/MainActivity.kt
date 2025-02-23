package me.thanish.prayers.se

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.thanish.prayers.se.theme.PrayersTheme
import me.thanish.prayers.se.worker.NotificationWorker
import me.thanish.prayers.se.worker.SchedulerWorker

/**
 * MainActivity is the main activity of the app.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize workers asynchronously
        CoroutineScope(Dispatchers.IO).launch {
            SchedulerWorker.initialize(applicationContext)
            NotificationWorker.initialize(applicationContext)
        }

        setContent {
            PrayersTheme {
                Navigation()
            }
        }
    }
}
