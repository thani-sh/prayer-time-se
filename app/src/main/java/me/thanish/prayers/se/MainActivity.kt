package me.thanish.prayers.se

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import me.thanish.prayers.se.routes.home.MainRoute
import me.thanish.prayers.se.ui.theme.PrayersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrayersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainRoute(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
