package com.github.jlcarveth.grocer.model

import org.junit.Test

import org.junit.Assert.*

/**
 * Created by John on 1/4/2018.
 */
class MinuteTimeTest {
    @Test
    fun getMinutes() {
        try {
            val a = MinuteTime(-200)
            assert(false)
        } catch (e : IllegalArgumentException) {
            //assert(true)
        }

        val b = MinuteTime(123)
        assertEquals(123, b.getMinutes())

        val c = MinuteTime(3)
        assertEquals(3, c.getMinutes())
    }

    @Test
    fun getHours() {
        val a = MinuteTime(123)
        assertEquals(2, a.getHours())

        val b = MinuteTime(3)
        assertEquals(0, b.getHours())
    }

    @Test
    fun getRemainingMinutes() {
        val a = MinuteTime(123)
        assertEquals(3, a.getRemainingMinutes())

        val b = MinuteTime(3)
        assertEquals(3, b.getRemainingMinutes())
    }

}