package com.cyberfox21.tinkoffmessanger.presentation

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

import android.graphics.Paint


class CustomMessageItemDecorator : RecyclerView.ItemDecoration() {

    private val textBoundsRectangle = Rect()
    private var paint = Paint()


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {


        val position = parent.getChildAdapterPosition(view)
        val holder = parent.getChildViewHolder(view) as DateViewHolder
        val currentTime = (parent.adapter as ChatRecyclerAdapter).getTime(position)
        val previosTime = if (position != 0) (parent.adapter as ChatRecyclerAdapter).getTime(position - 1) else ""

        paint.getTextBounds(currentTime, 0, currentTime.length, textBoundsRectangle)
        val textHeight = textBoundsRectangle.height()
        val textWidth = textBoundsRectangle.width()
        parent.adapter?.let {
            val viewType = it.getItemViewType(position)
            if (viewType == ChatRecyclerAdapter.ViewType.DATE_DEVIDER.ordinal) {
                val params = view.layoutParams as RecyclerView.LayoutParams
                val top = view.top + params.topMargin
                val bottom = top + textHeight
                outRect.set(0, view.top + params.topMargin, parent.right, bottom)
            } else {
                outRect.setEmpty()
            }
        }

    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            parent.adapter?.let {
                val viewType = it.getItemViewType(position)
                if (viewType == ChatRecyclerAdapter.ViewType.DATE_DEVIDER.ordinal) {
                    c.drawRect(
                        view.left.toFloat(),
                        view.bottom.toFloat(),
                        view.right.toFloat(),
                        view.bottom + textBoundsRectangle.height().toFloat(),
                        paint
                    )
                }
            }
        }

//        for (i in 0 until parent.childCount) {
//            val view = parent.getChildAt(i)
//            val position = parent.getChildAdapterPosition(view)
//            val currentTime = (parent.adapter as ChatRecyclerAdapter).getTime(position)
//            val previosTime =
//                if (position != 0) (parent.adapter as ChatRecyclerAdapter).getTime(position - 1) else ""
//            val textLayout = parent.context.resources.getLayout(R.layout.date_item_decoration)
//            //textLayout.text = currentTime
//            val d: Drawable = textLayout as Drawable
//            if (currentTime != previosTime) {
//                val params = view.layoutParams as RecyclerView.LayoutParams
//                val top = view.top + params.topMargin
//                val bottom = top + d.intrinsicHeight
//                d.setBounds(0, top, parent.right, bottom)
//                d.draw(c)
//            }
//        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

    }

}
