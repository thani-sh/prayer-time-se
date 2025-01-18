package me.thanish.prayers.se.widget.nextprayer

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import me.thanish.prayers.se.domain.PrayerTime

class Widget : GlanceAppWidget() {
    /**
     * stateDefinition is used to store the state of the widget. This affects
     * when the widget is updated and new data gets rendered on the widget.
     */
    override val stateDefinition: GlanceStateDefinition<PrayerTime> get() = WidgetState()

    /**
     * provideGlance is called when the widget is created and maybe after a long time interval.
     */
    override suspend fun provideGlance(context: Context, id: GlanceId) {
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

/**
 * RefreshAction is an action to refresh values shown on the widget.
 */
class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Widget().update(context, glanceId)
    }
}
