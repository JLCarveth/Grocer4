package com.github.jlcarveth.grocer.layout.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.github.jlcarveth.grocer.R
import com.github.jlcarveth.grocer.model.MinuteTime

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MinuteTimePickerDialog.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MinuteTimePickerDialog.newInstance] factory method to
 * create an instance of this fragment.
 */
class MinuteTimePickerDialog : DialogFragment() {

    var mode = HOUR_AND_MINUTE

    /**
     * Fields and titles
     */
    lateinit var hourTitle : TextView
    lateinit var minuteTitle : TextView
    lateinit var hourField : EditText
    lateinit var minuteField : EditText
    lateinit var switchBtn : Button

    lateinit var provider : MinuteTime.MinuteTimeProvider

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity.layoutInflater.inflate(R.layout.fragment_minute_time_picker_dialog, null)

        // Get arguments if any
        if (arguments != null) {
            if (arguments.getInt("mode") == HOUR_AND_MINUTE ||
                    arguments.getInt("mode") == MINUTE) {
                mode = arguments.getInt("mode")
            }
        }

        // Declare the views
        hourTitle = view.findViewById(R.id.mtpd_hourTitle)
        hourField  = view.findViewById(R.id.mtpd_hourField)

        minuteTitle = view.findViewById(R.id.mtpd_minuteTitle)
        minuteField = view.findViewById(R.id.mtpd_minuteField)

        switchBtn = view.findViewById(R.id.mtpd_switch)
        // Allows the user to switch between the dialog modes
        switchBtn.setOnClickListener {
            when(mode) {
                HOUR_AND_MINUTE -> mode = MINUTE
                MINUTE -> mode = HOUR_AND_MINUTE
            }
            changeViews()
        }

        // Change the views based on the mode
        changeViews()

        //Declare the AlertDialog.Builder
        val builder = AlertDialog.Builder(activity)
        builder.setView(view)

        builder.setPositiveButton("OK") { dialog, which ->
            Log.d(TAG, "Positive action. Mode($mode)")
            when(mode) {
                MINUTE -> {
                    val t = minuteField.text.toString()

                    try {
                       provider.provide(MinuteTime(t.toInt()))
                    } catch(e : NumberFormatException) {
                        e.printStackTrace()
                    }
                }

                HOUR_AND_MINUTE -> {
                    val m = minuteField.text.toString()
                    val h = hourField.text.toString()

                    try {
                        provider.provide(MinuteTime(h.toInt(), m.toInt()))
                    } catch (e : NumberFormatException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        return builder.create()
    }

    private fun changeViews() {
        when(mode) {
            HOUR_AND_MINUTE -> {
                hourTitle.visibility = View.VISIBLE
                hourField.visibility = View.VISIBLE
            }
            MINUTE -> {
                hourTitle.visibility = View.GONE
                hourField.visibility = View.GONE
            }
        }
    }

    companion object {
        /**
         * Both the hour and minute fields will be shown to the user
         */
        val HOUR_AND_MINUTE = 0

        /**
         * Only the minute field will be shown to the user
         */
        val MINUTE = 1

        val TAG = "MinuteTimePicker"
    }
}
