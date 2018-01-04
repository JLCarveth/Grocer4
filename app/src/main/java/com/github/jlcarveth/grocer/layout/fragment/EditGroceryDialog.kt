package com.github.jlcarveth.grocer.layout.fragment

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import com.github.jlcarveth.grocer.storage.DatabaseHandler

/**
 * Created by John on 12/14/2017.
 */
class EditGroceryDialog : DialogFragment() {

    private lateinit var dh : DatabaseHandler

    private val TAG = "EditDialog"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }
}