package com.github.jlcarveth.grocer.model

/**
 * Created by John on 1/4/2018.
 *
 * Simple class for handling short quantities of time and representing them nicely.
 */
class MinuteTime(private val minutes : Int) {
    init {
        if (minutes < 0) {
            throw IllegalArgumentException("Cannot be less than 0.")
        }
    }

    /**
     * Secondary Constructor supports hours and minutes
     */
    constructor(hours : Int, minutes : Int) : this((hours * 60) + minutes) {
        if (hours < 0) {
            throw IllegalArgumentException("Cannot be less than 0.")
        }
    }

    /**
     * Gets the total time in minutes
     */
    fun getTimeInMinutes() : Int {
        return minutes
    }

    /**
     * Gets the most hours contained in the minutes
     */
    fun getHours() : Int {
        return minutes / 60
    }

    /**
     * Gets the remaining minutes
     */
    fun getMinutes() : Int {
        return minutes - (getHours() * 60)
    }

    /**
     * Overriding the addition operator
     */
    operator fun plus(a : MinuteTime) : MinuteTime {
        return MinuteTime(a.getTimeInMinutes() + this.getTimeInMinutes())
    }

    /**
     * The String representation of our object.
     * Ex. MinuteTime(123) -> 2h 3m
     *     MinuteTime(12) -> 12m
     *     etc...
     */
    override fun toString() : String {
        if (getHours() == 0) {
            return "${this.getTimeInMinutes()}m"
        } else {
            return "${this.getHours()}h ${this.getMinutes()}m"
        }
    }
}