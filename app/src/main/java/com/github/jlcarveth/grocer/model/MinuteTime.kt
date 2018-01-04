package com.github.jlcarveth.grocer.model

import kotlin.math.IEEErem

/**
 * Created by John on 1/4/2018.
 */
class MinuteTime(private val minutes : Int) {
    constructor(hours : Int, minutes: Int) : this((hours * 60) + minutes)

    init {
        if (minutes < 0) {
            throw IllegalArgumentException("Minutes cannot be less than 0.")
        }
    }

    /**
     * Returns the minutes
     */
    fun getMinutes() : Int {
        return minutes
    }

    /**
     * Returns the amount of hours
     */
    fun getHours() : Int {
        return (minutes / 60)
    }

    /**
     * Returns the remaining minutes
     */
    fun getRemainingMinutes() : Int {
        return (minutes % 60)
    }

    operator fun plus(a : MinuteTime) : MinuteTime {
        return MinuteTime(a.getMinutes() + this.getMinutes())
    }
}