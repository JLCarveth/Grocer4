package com.github.jlcarveth.grocer.layout.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import com.github.jlcarveth.grocer.R
import com.github.jlcarveth.grocer.model.GroceryItem
import com.github.jlcarveth.grocer.storage.DatabaseHandler
import com.github.jlcarveth.grocer.util.InputValidator

/**
 * Created by John on 11/17/2017.
 *
 * Our DialogFragment class for adding Grocery items
 * into the Database.
 */
class AddGroceryDialog : DialogFragment() {

    private lateinit var dataHandler : DatabaseHandler

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater : LayoutInflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.fragment_add_dialog, null)

        val nameInput = view.findViewById<EditText>(R.id.ad_name_input)
        val noteInput = view.findViewById<EditText>(R.id.ad_note_input)
        val qtyInput = view.findViewById<EditText>(R.id.ad_qty_input)

        var message = "Add an Item:"

        dataHandler = DatabaseHandler(activity.applicationContext)

        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(view)
        builder.setCancelable(false)

        builder.setMessage(message)
        builder.setPositiveButton(R.string.add) { dialog, id ->
            Log.d(Companion.TAG, "Add Item")
            if (InputValidator.validateField(nameInput,
                    InputValidator.ValidationType.NON_EMPTY)) {
                val name = nameInput.text.toString()
                val note = noteInput.text.toString()
                val qty = qtyInput.text.toString()

                dataHandler.insertGroceryItem(GroceryItem(name,note,qty,false))
            }
        }

        builder.setNegativeButton(R.string.cancel, object: DialogInterface.OnClickListener {
            override fun onClick(dialog : DialogInterface, id : Int) {
                dialog.dismiss()
                Log.d(Companion.TAG, "Negative Action")
            }
        })

        return builder.create()

    }

    companion object {
        val TAG = "AddGroceryDialog"
    }
}