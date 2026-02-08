package com.ags.countdown

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.Clock
import java.time.ZonedDateTime

object WidgetUpdateScheduler {
    const val ACTION_WIDGET_UPDATE = "com.ags.countdown.action.WIDGET_UPDATE"
    private const val REQUEST_CODE = 3001

    fun scheduleNextUpdate(context: Context, clock: Clock = Clock.systemUTC()) {
        val now = ZonedDateTime.ofInstant(clock.instant(), CountdownConfig.zoneId)
        val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay(CountdownConfig.zoneId)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, CountdownWidgetProvider::class.java).apply {
            action = ACTION_WIDGET_UPDATE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            nextMidnight.toInstant().toEpochMilli(),
            pendingIntent
        )
    }
}
