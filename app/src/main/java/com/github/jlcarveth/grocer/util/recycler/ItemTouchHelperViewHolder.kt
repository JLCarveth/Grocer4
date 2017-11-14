package com.github.jlcarveth.grocer.util.recycler

/**
 * Created by John on 11/13/2017.
 *
 * Used with our ItemTouchHelper / RecyclerView combo
 */
interface ItemTouchHelperViewHolder {
    /**
     * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
     */
    fun onItemSelected()

    /**
     * Called when the {@link ItemTouchHelper} has completed the move/swipe, and the item state should
     * be cleared.
     */
    fun onItemClear()
}