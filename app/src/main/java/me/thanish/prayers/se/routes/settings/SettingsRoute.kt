package me.thanish.prayers.se.routes.settings

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.USE_EXACT_ALARM
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import me.thanish.prayers.se.R
import me.thanish.prayers.se.device.HANAFI_ENABLED
import me.thanish.prayers.se.device.RequestPermission
import me.thanish.prayers.se.domain.NotificationOffset
import me.thanish.prayers.se.domain.PrayerTimeCity
import me.thanish.prayers.se.domain.PrayerTimeMethod
import me.thanish.prayers.se.routes.RouteSpec
import me.thanish.prayers.se.routes.RouteType
import me.thanish.prayers.se.routes.settings.components.SelectCityDropdown
import me.thanish.prayers.se.routes.settings.components.SelectMethodDropdown
import me.thanish.prayers.se.routes.settings.components.SelectOffsetDropdown
import me.thanish.prayers.se.theme.PrayersTheme
import me.thanish.prayers.se.worker.SchedulerWorker

/**
 * Describes the route to use with navigation.
 */
val SettingsRouteSpec = RouteSpec(
    name = "settings",
    text = R.string.route_settings_name,
    type = RouteType.SECONDARY,
    icon = { Pair(Icons.Filled.Settings, Icons.Outlined.Settings) },
    content = { nav: NavController, modifier: Modifier -> SettingsRoute(nav, modifier) }
)

@Composable
fun SettingsRoute(nav: NavController, modifier: Modifier = Modifier) {
    var city by remember { mutableStateOf(PrayerTimeCity.get()) }
    var method by remember { mutableStateOf(PrayerTimeMethod.get()) }
    var offset by remember { mutableStateOf(NotificationOffset.get()) }

    val onCityChange = { selectedCity: PrayerTimeCity ->
        city = selectedCity
        PrayerTimeCity.set(selectedCity)
        if (NotificationOffset.isEnabled()) {
            SchedulerWorker.schedule(nav.context, city)
        }
    }

    val onMethodChange = { selectedMethod: PrayerTimeMethod ->
        method = selectedMethod
        PrayerTimeMethod.set(selectedMethod)
        if (NotificationOffset.isEnabled()) {
            SchedulerWorker.schedule(nav.context, city)
        }
    }

    val onOffsetChange = { selectedOffset: NotificationOffset ->
        offset = selectedOffset
        NotificationOffset.set(selectedOffset)
        if (NotificationOffset.isEnabled()) {
            SchedulerWorker.schedule(nav.context, city)
        }
    }

    if (NotificationOffset.isEnabled()) {
        RequestPermission(
            requestedPermissions = arrayOf(POST_NOTIFICATIONS, USE_EXACT_ALARM),
            handleSuccess = { },
            handleFailure = { }
        )
    }

    SettingsRouteView(
        city,
        onCityChange,
        method,
        onMethodChange,
        offset,
        onOffsetChange,
        modifier
    )
}

@Composable
fun SettingsRouteView(
    city: PrayerTimeCity,
    onCityChange: (PrayerTimeCity) -> Unit,
    method: PrayerTimeMethod,
    onMethodChange: (PrayerTimeMethod) -> Unit,
    offset: NotificationOffset,
    onOffsetChange: (NotificationOffset) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SelectCityDropdown(city, onCityChange)
            if (HANAFI_ENABLED) {
                SelectMethodDropdown(method, onMethodChange)
            }
            SelectOffsetDropdown(offset, onOffsetChange)
        }
    }
}

@Preview
@Composable
fun SettingsRoutePreview() {
    val city = PrayerTimeCity.stockholm
    val method = PrayerTimeMethod.shafi
    val offset = NotificationOffset(10)
    val onCityChange: (PrayerTimeCity) -> Unit = {}
    val onMethodChange: (PrayerTimeMethod) -> Unit = {}
    val onOffsetChange: (NotificationOffset) -> Unit = {}

    PrayersTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SettingsRouteView(
                city,
                onCityChange,
                method,
                onMethodChange,
                offset,
                onOffsetChange,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
