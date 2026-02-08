package com.ags.countdown

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.time.Clock

class CountdownWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
        WidgetUpdateScheduler.scheduleNextUpdate(context)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == WidgetUpdateScheduler.ACTION_WIDGET_UPDATE) {
            val manager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, CountdownWidgetProvider::class.java)
            val ids = manager.getAppWidgetIds(componentName)
            updateWidgets(context, manager, ids)
            WidgetUpdateScheduler.scheduleNextUpdate(context)
        }
    }

    private fun updateWidgets(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
        clock: Clock = Clock.systemUTC()
    ) {
        val state = CountdownCalculator.calculate(clock)
        val text = if (state.isExpired) {
            "Süre doldu"
        } else {
            context.getString(R.string.widget_text_placeholder, state.daysRemaining)
        }

        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_countdown)
            views.setTextViewText(R.id.widget_text, text)

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_text, pendingIntent)

            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
