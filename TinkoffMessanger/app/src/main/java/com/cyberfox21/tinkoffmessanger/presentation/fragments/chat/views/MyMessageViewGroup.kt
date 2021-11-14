package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.cyberfox21.tinkoffmessanger.R

class MyMessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    init {
        inflate(context, R.layout.my_message_viewgroup, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        require(childCount == 3) { "Child count should be 3, but was $childCount" }
        val time = getChildAt(0)
        val text = getChildAt(1)
        val flexBoxLayout = getChildAt(2)

        val marginBottom = (text.layoutParams as MarginLayoutParams).bottomMargin
        val marginRight = (text.layoutParams as MarginLayoutParams).marginEnd

        measureChildWithMargins(text, widthMeasureSpec, text.measuredWidth, heightMeasureSpec, 0)
        measureChildWithMargins(time, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(flexBoxLayout, widthMeasureSpec, 0, heightMeasureSpec, 0)

        val totalWidth =
            maxOf(text.measuredWidth, time.measuredWidth) + marginRight * 2
        val totalHeight = 3 * marginBottom + time.measuredHeight + text.measuredHeight + flexBoxLayout.measuredHeight

        val resultWidth = resolveSize(
            totalWidth + paddingRight + paddingLeft,
            widthMeasureSpec
        )
        val resultHeight = resolveSize(
            totalHeight + paddingTop + paddingBottom,
            heightMeasureSpec
        )

        setMeasuredDimension(resultWidth, resultHeight)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val time = getChildAt(0)
        val text = getChildAt(1)
        val flexBoxLayout = getChildAt(2)

        val marginBottom = (text.layoutParams as MarginLayoutParams).bottomMargin
        val marginRight = (text.layoutParams as MarginLayoutParams).marginEnd

        time.layout(
            measuredWidth - paddingLeft - marginRight - time.measuredWidth,
            0 + paddingTop + marginBottom,
            measuredWidth - paddingLeft - marginRight,
            0 + time.measuredHeight + paddingTop + marginBottom
        )

        text.layout(
            measuredWidth - paddingLeft - marginRight - text.measuredWidth,
            0 + paddingBottom + marginBottom + time.bottom,
            measuredWidth - text.width - marginRight,
            time.bottom + text.measuredHeight + paddingBottom + marginBottom
        )

        flexBoxLayout.layout(
            measuredWidth - maxOf(time.width, text.width) - paddingLeft - marginRight,
            text.bottom + paddingBottom + marginBottom,
            measuredWidth - paddingLeft - marginRight,
            text.bottom + flexBoxLayout.measuredHeight + paddingBottom + marginBottom
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }
}