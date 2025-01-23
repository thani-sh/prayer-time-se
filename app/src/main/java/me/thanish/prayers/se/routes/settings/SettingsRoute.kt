package me.thanish.prayers.se.routes.settings

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.SCHEDULE_EXACT_ALARM
import android.Manifest.permission.USE_EXACT_ALARM
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import me.thanish.prayers.se.R
import me.thanish.prayers.se.device.DEBUG_ENABLED
import me.thanish.prayers.se.device.RequestPermission
import me.thanish.prayers.se.domain.NotificationOffset
import me.thanish.prayers.se.domain.PrayerTimeCity
import me.thanish.prayers.se.domain.PrayerTimeMethod
import me.thanish.prayers.se.routes.RouteSpec
import me.thanish.prayers.se.routes.RouteType
import me.thanish.prayers.se.routes.settings.components.FeedbackIconButtons
import me.thanish.prayers.se.routes.settings.components.SelectCityDropdown
import me.thanish.prayers.se.routes.settings.components.SelectMethodDropdown
import me.thanish.prayers.se.routes.settings.components.SelectOffsetDropdown
import me.thanish.prayers.se.routes.settings.components.SettingsSectionWithTitle
import me.thanish.prayers.se.routes.settings.components.TestNotificationButton
import me.thanish.prayers.se.theme.PrayersTheme
import me.thanish.prayers.se.worker.NotificationWorker
import me.thanish.prayers.se.worker.SchedulerWorker

/**
 * Describes the route to use with navigation.
 */
val SettingsRouteSpec = RouteSpec(
    name = "settings",
    text = R.string.route_settings_name,
    type = RouteType.PRIMARY,
    icon = { Pair(Icons.Filled.Settings, Icons.Outlined.Settings) },
    content = { nav: NavController, modifier: Modifier -> SettingsRoute(nav, modifier) }
)

@Composable
fun SettingsRoute(nav: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var city by remember { mutableStateOf(PrayerTimeCity.get(context)) }
    var method by remember { mutableStateOf(PrayerTimeMethod.get(context)) }
    var offset by remember { mutableStateOf(NotificationOffset.get(context)) }

    val onCityChange = { selectedCity: PrayerTimeCity ->
        city = selectedCity
        PrayerTimeCity.set(context, selectedCity)
        if (NotificationOffset.isEnabled(context)) {
            SchedulerWorker.schedule(nav.context, city)
        }
    }

    val onMethodChange = { selectedMethod: PrayerTimeMethod ->
        method = selectedMethod
        PrayerTimeMethod.set(context, selectedMethod)
        if (NotificationOffset.isEnabled(context)) {
            SchedulerWorker.schedule(nav.context, city)
        }
    }

    val onOffsetChange = { selectedOffset: NotificationOffset ->
        offset = selectedOffset
        NotificationOffset.set(context, selectedOffset)
        if (NotificationOffset.isEnabled(context)) {
            SchedulerWorker.schedule(nav.context, city)
        }
    }

    val onTestNotification = { delay: Long ->
        NotificationWorker.scheduleTestNotification(context, delay)
    }

    if (NotificationOffset.isEnabled(context)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestPermission(
                requestedPermissions = arrayOf(POST_NOTIFICATIONS, USE_EXACT_ALARM),
                handleSuccess = { },
                handleFailure = { }
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            RequestPermission(
                requestedPermissions = arrayOf(SCHEDULE_EXACT_ALARM),
                handleSuccess = { },
                handleFailure = { }
            )
        } else {
            // No permissions needed for notifications and alarms on older versions
        }
    }

    SettingsRouteView(
        city,
        onCityChange,
        method,
        onMethodChange,
        offset,
        onOffsetChange,
        onTestNotification,
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
    onTestNotification: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SettingsSectionWithTitle(
            titleText = stringResource(R.string.route_settings_section_methodology),
            descriptionText = stringResource(R.string.route_settings_section_methodology_details),
        ) {
            SelectCityDropdown(city, onCityChange)
            SelectMethodDropdown(method, onMethodChange)
        }

        SettingsSectionWithTitle(
            titleText = stringResource(R.string.route_settings_section_notifications),
            descriptionText = stringResource(R.string.route_settings_section_notifications_details),
        ) {
            SelectOffsetDropdown(offset, onOffsetChange)
        }

        SettingsSectionWithTitle(
            titleText = stringResource(R.string.route_settings_section_development),
            descriptionText = stringResource(R.string.route_settings_section_development_details),
        ) {
            FeedbackIconButtons()
            if (DEBUG_ENABLED) {
                Spacer(Modifier.height(32.dp))
                TestNotificationButton(onTestNotification)
            }
        }
    }
}

@Preview
@Composable
fun SettingsRoutePreview() {
    val city = PrayerTimeCity.stockholm
    val method = PrayerTimeMethod.islamiskaforbundet
    val offset = NotificationOffset(10)
    val onCityChange: (PrayerTimeCity) -> Unit = {}
    val onMethodChange: (PrayerTimeMethod) -> Unit = {}
    val onOffsetChange: (NotificationOffset) -> Unit = {}
    val onTestNotification: (Long) -> Unit = {}

    PrayersTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SettingsRouteView(
                city,
                onCityChange,
                method,
                onMethodChange,
                offset,
                onOffsetChange,
                onTestNotification,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
