package kotyper.modes

import com.fasterxml.jackson.annotation.JsonIgnore

abstract class KotyperGameResult {
    @JsonIgnore
    abstract fun getCPS(): Double

    @JsonIgnore
    fun getCPM(): Double {
        return getCPS() * 60.0
    }

    @JsonIgnore
    fun getWPM(): Double {
        return getCPM() / 5
    }
}