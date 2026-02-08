package com.ags.countdown

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.Clock
import java.time.ZonedDateTime

object AlarmScheduler {
    private const val REQUEST_CODE = 2001

    fun scheduleOneDayBefore(context: Context, clock: Clock = Clock.systemUTC()) {
        val target = CountdownConfig.targetDateTime(clock)
        val oneDayBefore = target.minusDays(1)
        val now = ZonedDateTime.ofInstant(clock.instant(), CountdownConfig.zoneId)

        if (now.isAfter(oneDayBefore)) {
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            oneDayBefore.toInstant().toEpochMilli(),
            pendingIntent
        )
    }
}
