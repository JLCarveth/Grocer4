package com.github.jlcarveth.grocer.layout.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by John on 11/22/2017.
 */
class RecyclerViewDivider(var divider : Drawable) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent?.getChildAdapterPosition(view) == 0) {
            return;
        }

        outRect?.top = divider.intrinsicHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        val childCount = parent.childCount

        for (i in 0 until childCount) {
            var child : View = parent.getChildAt(i)
            var params = child.layoutParams as RecyclerView.LayoutParams

            var dividerTop = child.bottom + params.bottomMargin
            var dividerBottom = dividerTop + divider.intrinsicHeight

            divider.setBounds(dividerLeft,dividerTop,dividerRight,dividerBottom)
            divider.draw(c)
        }
    }
}