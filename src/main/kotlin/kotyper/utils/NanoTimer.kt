package kotyper.utils

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class NanoTimer {
    private var startTime: Long = 0
    private var finishTime: Long = 0

    fun start() {
        startTime = System.nanoTime()
    }

    fun stop() {
        finishTime = System.nanoTime()
    }

    @OptIn(ExperimentalTime::class)
    fun convertTo(durationUnit: DurationUnit): Double {
        return Duration.convert((finishTime - startTime).toDouble(), DurationUnit.NANOSECONDS, durationUnit)
    }

    fun getDelta(): Long {
        return finishTime - startTime
    }

}