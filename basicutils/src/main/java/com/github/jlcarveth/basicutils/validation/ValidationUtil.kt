package com.github.jlcarveth.basicutils.validation

import android.widget.EditText
import java.lang.NumberFormatException

/**
 * Created by John on 2/12/2018.
 *
 * Extension functions for String validation of many types.
 */
/**
 * @return true if that is the string's length, false otherwise
 */
fun String.validateLength(length : Int) : Boolean {
    return (this.length == length)
}

/**
 * @return true if the String only contains numerical characters
 */
fun String.validateNumeric() : Boolean {
    return try {
        Integer.parseInt(this)
        true
    } catch (e : NumberFormatException) {
        false
    }
}

/**
 * @return true if this string only has alphabetic chars
 */
fun String.validateAlpha() : Boolean {
    return !this.contains("{([0-9])}")
}

/**
 *
 */
fun EditText.isEmpty() : Boolean {
    return this.text.isEmpty()
}
