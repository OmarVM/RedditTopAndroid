package com.example.reddittop.view.adapter.swipeaction

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.reddittop.R

class ItemTouchHelperSwipe (private val callback: CallbackOnSwipeAction, private val mContext: Context?) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    val ALPHA_FULL = 1.0f
    val ALPHA_MAGNIFICATION = 1.2f
    private val mBackground = ColorDrawable()
    private val backgroundColor = Color.parseColor("#d32f2f")
    private val deleteIcon = mContext?.let { ContextCompat.getDrawable(it, R.drawable.ic_bin_trash_swipe) }
    private val intrinsicWidth = deleteIcon?.intrinsicWidth
    private val intrinsicHeight = deleteIcon?.intrinsicHeight

    interface CallbackOnSwipeAction{
        fun onSwipedItem(position: Int)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callback.onSwipedItem(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

            val mView = viewHolder.itemView
            val itemHeight = mView.bottom - mView.top
            val isCancelled: Boolean = dX == 0f && !isCurrentlyActive

            if (isCancelled){
                cleanCanvas(c, mView.right + dX, mView.top.toFloat(), mView.right.toFloat(), mView.bottom.toFloat())
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                return
            }

            mBackground.color = backgroundColor
            mBackground.setBounds(mView.right + dX.toInt(), mView.top, mView.right, mView.bottom)
            mBackground.draw(c)

        val deleteIconTop = mView.top + (itemHeight - intrinsicHeight!!) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = mView.right - deleteIconMargin - intrinsicWidth!!
        val deleteIconRight = mView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon?.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun cleanCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float){
        val mCleanPaint = Paint()
        mCleanPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        c.drawRect(left, top, right, bottom, mCleanPaint)
    }
}