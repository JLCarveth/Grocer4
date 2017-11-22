package com.github.jlcarveth.grocer.util

import android.widget.EditText

/**
 * Created by John on 11/17/2017.
 *
 * The purpose of this class is to handle all form validation,
 * so all facets of the app have the same standards for their fields.
 *
 * Perhaps implement some listener to update the fields if they are invalid?
 */
class InputValidator {

    enum class ValidationType {
        NON_EMPTY, NUMBER
    }

    companion object {

        /**
         * Validates a list of strings to make sure they are all empty
         */
        fun validateNotEmpty(strings : Collection<String>) : Boolean {
            val iterator = strings.iterator()
            var valid = true

            while (iterator.hasNext()) {
                val string = iterator.next()

                if (string.isEmpty()) {
                    valid = false
                }
            }

            return valid
        }

        fun validateNotEmpty(s : String) : Boolean = validateNotEmpty(arrayListOf(s))

        /**
         * Validates fields that should only contain numbers
         * @return true if the string is a valid number
         */
        fun validateNumbers(strings : Collection<String>) : Boolean {
            val iterator = strings.iterator()

            while (iterator.hasNext()) {
                try {
                    iterator.next().toInt()
                } catch (e : NumberFormatException) {
                    return false
                }
            }
            return true
        }

        /**
         * Validates the text in the provided EditText with the specified
         * validation type. (ex, Field non empty, field has valid number, etc)
         *
         * This class should be used in conjunction with EditText inputType
         * so all inputs are safest.
         */
        fun validateField(field : EditText, type : ValidationType) : Boolean = when (type) {
            ValidationType.NON_EMPTY -> validateNotEmpty(field.text.toString())
            ValidationType.NUMBER -> validateNotEmpty(field.text.toString())
        }
    }
}