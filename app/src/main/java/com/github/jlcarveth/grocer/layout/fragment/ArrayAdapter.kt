package com.github.jlcarveth.grocer.layout.fragment

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.jlcarveth.grocer.model.GroceryItem
import com.github.jlcarveth.grocer.storage.DatabaseHandler

/**
 * Created by John on 11/27/2017.
 *
 * An ArrayAdapter implementation for RecyclerView
 */
class ArrayAdapter<E>(val data: ArrayList<E>, c: Context) : RecyclerView.Adapter<ArrayAdapter.ViewHolder>() {
    private val TAG = "ArrayAdapter"

    private var dh : DatabaseHandler = DatabaseHandler(c)

    override fun getItemCount(): Int = data.count()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return

        holder.bind(data[position].toString())
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val g  = data[position];
                dh.showGroceryItem(g.toString())
                Closable.closeObservers()
            }
        })
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        // The ID from the Android layout file
        val textView = itemView?.findViewById<TextView>(android.R.id.text1)

        fun bind(s : String) {
            textView?.text = s
        }
    }
}

interface Closable {
    fun close()

    companion object {
        val observers = ArrayList<Closable>()

        fun attatch(observer : Closable) {
            observers.add(observer)
        }

        fun detach(observer : Closable) {
            observers.remove(observer)
        }

        fun closeObservers() {
            for (i in observers) {
                i.close()
            }
        }
    }
}

