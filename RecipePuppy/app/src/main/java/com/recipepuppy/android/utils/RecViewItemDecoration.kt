package com.recipepuppy.android.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class RecViewItemDecoration(val space: Int) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        //applies top margin only for first item
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.set(space, space, space, space)
        } else {
            outRect.set(space, 0, space, space)

        }
    }
}