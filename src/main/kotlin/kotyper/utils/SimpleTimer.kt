package kotyper.utils


import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class SimpleTimer {
    private var startTime = 0L
    private var duration = 0L
    private val finishTime
        get() = startTime + duration

    @OptIn(ExperimentalTime::class)
    fun setupFor(time: Double, unit: DurationUnit) {
        duration = Duration.convert(time, unit, DurationUnit.MILLISECONDS).toLong()
    }

    fun start(time: Double, unit: DurationUnit) {
        setupFor(time, unit)
        start()
    }

    fun start() {
        startTime = System.currentTimeMillis()
    }

    fun getRest(): Long {
        return finishTime - System.currentTimeMillis()
    }

    fun isExpired(): Boolean {
        return finishTime <= System.currentTimeMillis()
    }

    fun isStarted(): Boolean {
        return startTime != 0L
    }
}