package com.github.jlcarveth.grocer.util.recycler

/**
 * Created by John on 11/14/2017.
 *
 * An interface which is used with our ItemTouchHelper
 * Provides the methods for item events regarding dragging / swiping
 */
interface ItemTouchHelperAdapter {
    /**
     * Called when an item has been dragged far enough to trigger a move.
     *
     * @param fromPosition The start position of the moved item.
     * @param toPosition   Then resolved position of the moved item.
     * @return True if the item was moved to the new adapter position.
     *
     * @see RecyclerView#getAdapterPositionFor(RecyclerView.ViewHolder)
     * @see RecyclerView.ViewHolder#getAdapterPosition()
     */
    fun onItemMove(from : Int, to : Int) : Boolean

    /**
     * Called when an item has been dismissed by a swipe.
     * @param position the position of the item dismissed.
     */
    fun onItemDismiss(position : Int)
}