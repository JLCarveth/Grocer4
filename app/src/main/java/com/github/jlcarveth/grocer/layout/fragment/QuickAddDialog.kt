package com.github.jlcarveth.grocer.layout.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import com.github.jlcarveth.grocer.R
import com.github.jlcarveth.grocer.layout.decoration.RecyclerViewDivider
import com.github.jlcarveth.grocer.model.GroceryItem
import com.github.jlcarveth.grocer.storage.DatabaseHandler

/**
 * Created by John on 11/26/2017.
 */
class QuickAddDialog : DialogFragment(), Closable {

    private lateinit var dataHandler : DatabaseHandler

    private lateinit var adapter : ArrayAdapter<GroceryItem>

    private val TAG = "QuickAddDialog"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater : LayoutInflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.quickadd_layout, null)
        val rv : RecyclerView = view.findViewById(R.id.quickadd_list)
        val divider = ContextCompat.getDrawable(activity, R.drawable.divider) as Drawable
        val div = RecyclerViewDivider(divider)
        dataHandler = DatabaseHandler(activity.applicationContext)

        val dataset = dataHandler.getHiddenGroceryItems()

        Closable.attatch(this)
        adapter = ArrayAdapter(dataset, activity.applicationContext)
        rv.adapter = adapter
        rv.addItemDecoration(div)


        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(view)
        builder.setCancelable(false)

        builder.setMessage("Quick-Add an Item:")

        builder.setNegativeButton(R.string.cancel) { dialog, id ->
            dialog.dismiss()
            Log.d(TAG, "Cancelled")
        }

        return builder.create()
    }

    override fun close() {
        this.dismiss()
    }

}