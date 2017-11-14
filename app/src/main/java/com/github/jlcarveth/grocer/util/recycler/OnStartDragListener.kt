package com.github.jlcarveth.grocer.util.recycler

import android.support.v7.widget.RecyclerView

/**
 * Created by John on 11/14/2017.
 *
 * Interface for ItemTouchHelper. Listens to draggable views
 */
interface OnStartDragListener {
    /**
     *  Called when a view is requesting the start of a drag
     *
     *  @param viewHolder the holder of the view to drag
     */
    fun onStartDrag(viewHolder : RecyclerView.ViewHolder)
}