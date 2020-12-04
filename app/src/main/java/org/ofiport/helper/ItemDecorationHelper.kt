package org.ofiport.helper

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView


/**
 * @author farhan
 * created at at 21:55 on 17/10/2020.
 */
class ItemDecorationHelper : RecyclerView.ItemDecoration {
  private var mItemOffset = 0
  private var mPosition = BOTH

  companion object {
    const val BOTH = 0
    const val TOP = 1
    const val BOTTOM = 2
  }

  constructor(itemOffset: Int) {
    mItemOffset = itemOffset
  }

  constructor(context: Context, @DimenRes itemOffsetId: Int) {
    mItemOffset = context.resources.getDimensionPixelSize(itemOffsetId)
  }

  constructor(context: Context, @DimenRes itemOffsetId: Int, position: Int) {
    mItemOffset = context.resources.getDimensionPixelSize(itemOffsetId)
    mPosition = position
  }

  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: RecyclerView.State
  ) {
    super.getItemOffsets(outRect, view, parent, state)
    parent.adapter?.let {
      if (parent.getChildAdapterPosition(view) == 0 && (mPosition == TOP || mPosition == BOTH)) outRect.top =
        mItemOffset
      else if (parent.getChildAdapterPosition(view) == it.itemCount - 1 && (mPosition == BOTTOM || mPosition == BOTH)) outRect.bottom =
        mItemOffset
    }
  }
}