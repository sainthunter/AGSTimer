package com.ags.countdown

import java.time.Clock
import java.time.Duration
import java.time.ZonedDateTime

object CountdownCalculator {
    data class CountdownState(
        val daysRemaining: Long,
        val hours: Long,
        val minutes: Long,
        val seconds: Long,
        val isExpired: Boolean
    )

    fun calculate(clock: Clock): CountdownState {
        val now = ZonedDateTime.ofInstant(clock.instant(), CountdownConfig.zoneId)
        val target = CountdownConfig.targetDateTime(clock)
        val duration = Duration.between(now, target)

        if (duration.isNegative || duration.isZero) {
            return CountdownState(0, 0, 0, 0, true)
        }

        val totalSeconds = duration.seconds
        val days = totalSeconds / 86_400
        val remainderAfterDays = totalSeconds % 86_400
        val hours = remainderAfterDays / 3_600
        val remainderAfterHours = remainderAfterDays % 3_600
        val minutes = remainderAfterHours / 60
        val seconds = remainderAfterHours % 60

        return CountdownState(days, hours, minutes, seconds, false)
    }
}
