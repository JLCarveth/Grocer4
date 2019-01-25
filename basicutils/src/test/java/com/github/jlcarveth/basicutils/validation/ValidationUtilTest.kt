package com.github.jlcarveth.basicutils.validation

import org.junit.Test

import org.junit.Assert.*

/**
 * Created by John on 2/12/2018.
 */
class ValidationUtilTest {
    @Test
    fun validateLength() {
        val s1 = ""
        val s2 = "John"
        val s3 = "mmmhmhmhmhmhm329"

        assertTrue(s1.validateLength(0))
        assertTrue(s2.validateLength(4))
        assertFalse(s3.validateLength(-4))
    }

    @Test
    fun validateNumeric() {
    }

    @Test
    fun validateAlpha() {
    }

}