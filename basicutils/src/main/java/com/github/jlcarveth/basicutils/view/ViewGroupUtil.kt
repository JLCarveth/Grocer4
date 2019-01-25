package com.github.jlcarveth.basicutils.view

import android.view.View
import android.view.ViewGroup

/**
 * Created by John on 1/9/2018.
 *
 * A helper class for swapping View objects.
 */
object ViewGroupUtil {

    /**
     * @return the views parent as a ViewGroup, or null if there is no parent
     */
    private fun getParent(v : View) : ViewGroup? {
        if (v.parent == null) {
            return null
        }
        return v.parent as ViewGroup
    }

    /**
     * Removes the view from its parent
     * @param v the view to remove
     */
    private fun removeView(v : View) {
        val parent = getParent(v)

        parent?.removeView(v)
    }

    /**
     * Replaces one view with another. Can be used anywhere. 
     * @param old the View being replaced
     * @param new the replacing View
     */
    fun replaceView(old : View, new : View) {
        val parent = getParent(old) ?: return

        val index = parent.indexOfChild(old)
        removeView(old)
        removeView(new)
        parent.addView(new, index)
    }
}