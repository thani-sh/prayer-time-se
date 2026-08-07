package me.thanish.prayers.se.widget.nextprayer

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.currentState
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import me.thanish.prayers.se.domain.PrayerTime
import me.thanish.prayers.se.worker.NotificationWorker

class Widget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<PrayerTime> get() = WidgetState()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Ensure minute ticker is scheduled when widget is provided
        NotificationWorker.scheduleMinuteTick(context)

        provideContent {
            val prayerTime = currentState<PrayerTime>()
            val onClickAction = actionRunCallback<RefreshAction>()

            GlanceTheme(GlanceTheme.colors) {
                Box(GlanceModifier.fillMaxSize().clickable(onClick = onClickAction)) {
                    WidgetContent(prayerTime)
                }
            }
        }
    }
}

suspend fun hasActiveWidgets(context: Context): Boolean {
    return try {
        GlanceAppWidgetManager(context).getGlanceIds(Widget::class.java).isNotEmpty()
    } catch (e: Exception) {
        false
    }
}

suspend fun updateAllWidgets(context: Context) {
    try {
        if (!hasActiveWidgets(context)) return
        Widget().updateAll(context)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Widget().update(context, glanceId)
    }
}
