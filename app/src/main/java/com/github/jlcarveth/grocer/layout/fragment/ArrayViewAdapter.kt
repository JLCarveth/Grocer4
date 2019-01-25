package com.github.jlcarveth.grocer.layout.fragment

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.jlcarveth.grocer.R
import com.github.jlcarveth.grocer.storage.DatabaseHandler
import com.github.jlcarveth.grocer.util.ViewGroupUtil

/**
 * Created by John on 11/27/2017.
 *
 * An ArrayViewAdapter implementation for RecyclerView
 * It can be given an ArrayList of any class that extends View.
 */
class ArrayViewAdapter<E>(val data: ArrayList<E>, c: Context) :
        RecyclerView.Adapter<ArrayViewAdapter.ViewHolder>() where E : View {
    private val TAG = "ArrayViewAdapter"

    private var dh : DatabaseHandler = DatabaseHandler(c)

    override fun getItemCount(): Int = data.count()

    /**
     * Inflate our Linear
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.layout_linear, parent, false)
        return ViewHolder(view)
    }

    /**
     * Attaches the View to the ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return

        holder.bind(data[position])
    }

    /**
     * Clears all data from the Adapter
     */
    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    /**
     * Adds the collection to the Adapter
     */
    fun addAll(a : Collection<E>) {
        data.addAll(a)
        notifyDataSetChanged()
    }

    /**
     * Our ViewHolder, which consists of a simple LinearLayout object
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // The ID of the TableLayout
        val ll_view = itemView.findViewById<View>(R.id.ll_view)

        fun bind(v : View) {
            ViewGroupUtil.replaceView(ll_view, v)
        }
    }
}


