package com.github.jlcarveth.grocer.util.recycler

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log

/**
 * Created by John on 11/14/2017.
 *
 * Our Class that handles the callbacks from the listeners.
 * This class allows for some customization on the functionality of
 * the swiping / dragging behaviours.
 */

val TAG : String = "TouchHelperCallback"

class SimpleTouchHelperCallback : ItemTouchHelper.Callback {
    lateinit var adapter : ItemTouchHelperAdapter

    constructor(adapter : ItemTouchHelperAdapter) {
        this.adapter = adapter
    }

    override fun isLongPressDragEnabled(): Boolean = false

    override fun isItemViewSwipeEnabled(): Boolean = true

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val dragFlags : Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags : Int = ItemTouchHelper.START

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        if (viewHolder?.itemViewType != target?.itemViewType) return false

        adapter.onItemMove(viewHolder!!.adapterPosition, target!!.adapterPosition)

        return true
    }

    /**
     * Called when a ViewHolder is swiped by the user.
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Log.d(TAG, "Swiped @ ${viewHolder.adapterPosition}");
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }

    /**
     * Called by ItemTouchelper on RecyclerView's onDraw callback
     */
    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val alpha: Float = 1.0F - Math.abs(dX) / viewHolder.itemView.width
            viewHolder.itemView.alpha = alpha
            viewHolder.itemView.translationX = dX
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    /**
     * Called when the ViewHolder swiped or dragged by the ItemTouchHelper is changed
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is ItemTouchHelperViewHolder) {
                viewHolder.onItemSelected()
            }
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    /**
     * Called when a view is cleared (swiped to dismiss)
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        viewHolder.itemView.alpha =1.0F

        if (viewHolder is ItemTouchHelperViewHolder) {
            viewHolder.onItemClear()
        }

    }
}