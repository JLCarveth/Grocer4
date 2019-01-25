package com.github.jlcarveth.grocer.layout.fragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by John on 1/17/2018.
 *
 * Simple Array Adapter that displays a list of Objects by their toString() values.
 * Good for quick simple lists.
 */
class ArrayAdapter<E>(val data : ArrayList<E>) : RecyclerView.Adapter<ArrayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return

        holder.bind(data[position].toString())
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        // The ID from the Android layout file
        val textView = itemView?.findViewById<TextView>(android.R.id.text1)

        fun bind(s : String) {
            textView?.text = s
        }
    }
}