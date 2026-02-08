package com.ags.countdown

import java.time.Clock
import java.time.ZoneId
import java.time.ZonedDateTime

object CountdownConfig {
    val zoneId: ZoneId = ZoneId.of("Europe/Istanbul")
    private val fixedTarget: ZonedDateTime = ZonedDateTime.of(2026, 7, 12, 10, 0, 0, 0, zoneId)

    // Debug için geçici hedef: şimdi + DEBUG_TARGET_OFFSET_MINUTES.
    private const val DEBUG_ONLY_USE_NEAR_TARGET = false
    private const val DEBUG_TARGET_OFFSET_MINUTES = 5L

    fun targetDateTime(clock: Clock): ZonedDateTime {
        if (BuildConfig.DEBUG && DEBUG_ONLY_USE_NEAR_TARGET) {
            return ZonedDateTime.ofInstant(clock.instant(), zoneId)
                .plusMinutes(DEBUG_TARGET_OFFSET_MINUTES)
        }
        return fixedTarget
    }
}
