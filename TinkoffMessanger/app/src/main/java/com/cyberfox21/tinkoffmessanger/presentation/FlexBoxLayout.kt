package com.cyberfox21.tinkoffmessanger.presentation

import android.content.Context
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

    private var marginRight = 0
    private var marginBottom = 0

    private var endPadding = 0

    init {
        inflate(context, R.layout.flexboxlayout_with_add_button, this)
        marginRight = FLEXBOX_MARGIN_RIGHT
        marginBottom = FLEXBOX_MARGIN_BOTTOM
        endPadding = resources.getDimension(R.dimen.padding20dp).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val width = getSize(widthMeasureSpec) - paddingLeft - endPadding - 2 * marginRight

        val btnAdd = getChildAt(0)
        btnAdd.measure(widthMeasureSpec, heightMeasureSpec)

        var totalWidth = 0
        var totalHeight = btnAdd.measuredHeight
        var currentWidth = btnAdd.measuredWidth

        if (childCount > 1) {
            var child = getChildAt(1)
            for (i in 1 until childCount) {
                child = getChildAt(i)
                child.measure(widthMeasureSpec, heightMeasureSpec)
                if (currentWidth != 0) currentWidth += marginRight
                if (btnAdd.width + child.width + currentWidth + marginRight + endPadding > width) {
                    if (child.width + currentWidth + marginRight + endPadding <= width) {
                        currentWidth += child.width
                        totalHeight += marginBottom + child.height
                    } else {
                        totalHeight += marginBottom + child.height
                        currentWidth = 0
                    }
                } else {
                    currentWidth += child.width
                    totalWidth = maxOf(currentWidth, totalWidth)
                }
            }
            totalWidth += marginRight
            totalHeight += marginBottom + child.measuredHeight
        }

        val resultWidth = resolveSize(
            paddingLeft + totalWidth + paddingRight,
            widthMeasureSpec
        )
        val resultHeight = resolveSize(
            paddingBottom + totalHeight + paddingTop,
            heightMeasureSpec
        )

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {

        var currentBottom = 0
        var currentRight = 0

        val btnAdd = getChildAt(0)

        if (childCount > 1) {
            var child = getChildAt(1)
            for (i in 1 until childCount) {
                child = getChildAt(i)
                if (currentRight + child.measuredWidth + endPadding > width) {
                    currentRight = 0
                    currentBottom += marginBottom + child.measuredHeight
                } else if (currentRight != 0) {
                    currentRight += marginRight
                }
                child.layout(
                    currentRight,
                    currentBottom,
                    currentRight + child.measuredWidth,
                    currentBottom + child.measuredHeight
                )
                currentRight += child.measuredWidth
            }
            if (currentRight + child.measuredWidth > width) {
                currentRight = 0
                currentBottom += marginBottom + child.measuredHeight
            }
            if (currentRight != 0) currentRight += marginRight
            btnAdd.layout(
                currentRight,
                currentBottom,
                currentRight + child.measuredWidth,
                currentBottom + child.measuredHeight
            )
        } else {
            btnAdd.layout(
                0,
                0,
                btnAdd.measuredWidth,
                btnAdd.measuredHeight
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
        const val FLEXBOX_MARGIN_RIGHT = 20
        const val FLEXBOX_MARGIN_BOTTOM = 20
    }

}
