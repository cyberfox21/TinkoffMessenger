package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View.MeasureSpec.getSize
import android.view.ViewGroup
import com.cyberfox21.tinkoffmessanger.R

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var percent = 0f
        set(value) {
            field = value
            requestLayout()
        }
    var horizontalMargin = 0
        set(value) {
            field = value
            requestLayout()
        }
    var verticalMargin = 0
        set(value) {
            field = value
            requestLayout()
        }

    init {

        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.FlexBoxLayout,
            defStyleAttr,
            defStyleRes
        )
        percent = typedArray.getFloat(R.styleable.FlexBoxLayout_percent, DEFAULT_VALUE_PERCENT)
        horizontalMargin = typedArray.getDimension(
            R.styleable.FlexBoxLayout_horizontalMargins,
            DEFAULT_VALUE_HORIZONTAL_MARGIN
        ).toInt()
        verticalMargin = typedArray.getDimension(
            R.styleable.FlexBoxLayout_verticalMargins,
            DEFAULT_VALUE_VERTICAL_MARGIN
        ).toInt()
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val width = (getSize(widthMeasureSpec) - paddingLeft - paddingRight) * percent

        var totalWidth = 0
        var totalHeight = 0

        var currentWidth = 0
        var currentHeight = 0

        var rowMaxHeight = 0

        for (i in 0 until childCount) {

            val child = getChildAt(i)
            child.measure(widthMeasureSpec, heightMeasureSpec)

            if (currentWidth + child.measuredWidth + horizontalMargin > width) {
                currentWidth = 0
                currentHeight += rowMaxHeight
            }

            val isFirstRow = currentHeight == 0
            val isFirstCol = currentWidth == 0

            if (isFirstCol.not()) currentWidth += horizontalMargin
            currentWidth += child.measuredWidth

            rowMaxHeight = if (isFirstRow.not()) {
                maxOf(rowMaxHeight, verticalMargin + child.measuredHeight)
            } else {
                maxOf(rowMaxHeight, child.measuredHeight)
            }

            totalWidth = maxOf(totalWidth, currentWidth)
            totalHeight = maxOf(totalHeight, currentHeight + rowMaxHeight)
        }

        val resultWidth = resolveSize(paddingStart + totalWidth + paddingEnd, widthMeasureSpec)
        val resultHeight = resolveSize(paddingTop + totalHeight + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {

        var currentWidth = 0
        var currentHeight = 0

        var rowMaxHeight = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            if (currentWidth + child.measuredWidth + horizontalMargin > measuredWidth) {
                currentWidth = 0
                currentHeight += rowMaxHeight
            }

            val isFirstRow = currentHeight == 0
            val isFirstCol = currentWidth == 0

            if (!isFirstCol) currentWidth += horizontalMargin

            val top = if (isFirstRow) currentHeight else currentHeight + verticalMargin
            val left = currentWidth

            currentWidth += child.measuredWidth

            rowMaxHeight = if (isFirstRow.not()) {
                maxOf(rowMaxHeight, child.measuredHeight + verticalMargin)
            } else maxOf(rowMaxHeight, child.measuredHeight)

            child.layout(
                left,
                paddingTop + top,
                left + child.measuredWidth,
                paddingTop + top + child.measuredHeight
            )
        }

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

    companion object {
        const val DEFAULT_VALUE_PERCENT = 1.0f
        const val DEFAULT_VALUE_HORIZONTAL_MARGIN = 0f
        const val DEFAULT_VALUE_VERTICAL_MARGIN = 0f
    }

}
