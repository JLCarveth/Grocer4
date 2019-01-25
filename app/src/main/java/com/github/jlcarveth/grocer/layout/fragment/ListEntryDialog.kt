package com.github.jlcarveth.grocer.layout.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.github.jlcarveth.grocer.R

/**
 * Created by John on 1/17/2018.
 *
 * A DialogFragment
 */
class ListEntryDialog : DialogFragment() {

    private val texts = ArrayList<String>()

    lateinit var listable : Listable<String>

    var message = "Enter some lines of text"
    var title = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity.layoutInflater.inflate(R.layout.fragment_list_entry_dialog, null)

        val titleView = view.findViewById<TextView>(R.id.led_title)

        if (arguments != null) {
            title = arguments.getString("title")
            titleView.text = title
            message = arguments.getString("message")
        }

        val ed1 = view.findViewById<EditText>(R.id.led_ed1)

        ed1.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                texts.add(v.text.toString())
                Log.d(TAG, v.text.toString())
                v.text = ""
                titleView.text = "$title (${texts.count()})"
                return false
            }

        })

        val builder = AlertDialog.Builder(activity)
        builder.setView(view)
        builder.setMessage(message)

        builder.setPositiveButton(R.string.ok) { dialog, which ->
            Log.d(TAG, "Positive Action")
            //Need to save whatever is in the EditText when OK is pressed.
            if (!ed1.text.isEmpty()) {
                texts.add(ed1.text.toString())
            }
            listable.list(texts)
        }

        builder.setNegativeButton(R.string.cancel, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
                Log.d(TAG, "Cancelled.")
            }

        })

        return builder.create()
    }



    companion object {
        /**
         * Since Fragments need to have a default constructor so Android can restore its
         * activity's state, we add our title param here.
         */
        fun newInstance(title : String, message : String) : ListEntryDialog {
            val fragment = ListEntryDialog()

            fragment.arguments.putString("title", title)
            fragment.arguments.putString("message", message)

            return fragment
        }

        val TAG = "ListEntryDialog"
    }
}

interface Listable<E> {
    /**
     * For Dialogs that need to pass a list to another class
     */
    fun list(data : MutableList<E>)

}