package com.github.jlcarveth.grocer.model

import org.junit.Test

import org.junit.Assert.*
import kotlin.test.assertFailsWith

/**
 * Created by John on 1/4/2018.
 */
class MinuteTimeTest {

    @Test
    fun testNegativeTime() {
        assertFailsWith(IllegalArgumentException::class) {
            MinuteTime(-1)
        }
    }
    @Test
    fun getTimeInMinutes() {
        try {
            val a = MinuteTime(-5)
            assert(false)
        } catch (e : IllegalArgumentException) {

        }

        val b = MinuteTime(5)
        assertEquals(5, b.getTimeInMinutes())
        val c = MinuteTime(196)
        assertEquals(196, c.getTimeInMinutes())
        val d = MinuteTime(1002)
        assertEquals(1002, d.getTimeInMinutes())

        println(b.toString())
        println(c.toString())
        println(d.toString())
    }

    @Test
    fun getHours() {
    }

    @Test
    fun getMinutes() {
    }

    @Test
    fun plus() {
    }


}