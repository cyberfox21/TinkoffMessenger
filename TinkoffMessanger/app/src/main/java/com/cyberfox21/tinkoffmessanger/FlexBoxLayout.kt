package com.cyberfox21.tinkoffmessanger

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View.MeasureSpec.getSize
import android.view.ViewGroup
import androidx.core.view.marginTop

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private var marginRight = 0
    private var marginBottom = 0

    init {
        inflate(context, R.layout.flexboxlayout_with_add_button, this)
        marginRight = 20
        marginBottom = 20
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d("FlexBoxLayout", "childCount $childCount")

        val width = getSize(widthMeasureSpec) - paddingLeft - paddingRight + 2 * marginRight

        var totalWidth = 0
        var totalHeight = 0
        var withMargins = 0 // if the first - one side margin // else two side margins

        val btnAdd = getChildAt(0)
        btnAdd.measure(widthMeasureSpec, heightMeasureSpec)

        if (childCount > 1) {
            for (i in 1 until childCount) {
                val child = getChildAt(i)
                child.measure(widthMeasureSpec, heightMeasureSpec)
                if (withMargins != 0) withMargins += marginRight
                if (btnAdd.measuredWidth + child.measuredWidth + withMargins + marginRight > width) {
                    if (child.measuredWidth + withMargins + marginRight <= width) {
                        withMargins += child.measuredWidth
                    } else {
                        totalHeight += marginBottom + child.measuredHeight
                        withMargins += 0
                    }
                } else {
                    withMargins += child.measuredWidth
                    totalWidth = maxOf(withMargins, totalWidth)
                    totalHeight = maxOf(child.measuredHeight, totalHeight)
                }
            }
            totalHeight += marginBottom + btnAdd.measuredHeight
            totalWidth += marginRight + btnAdd.measuredWidth
        } else {

            val child = getChildAt(0)
            child.measure(widthMeasureSpec, heightMeasureSpec)
            totalHeight = child.measuredHeight
            totalWidth = child.measuredWidth
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
            for (i in 1 until childCount) {
                val child = getChildAt(i)
                if (btnAdd.measuredWidth + currentRight + child.measuredWidth + marginRight > this.width) {
                    if (currentRight + child.measuredWidth > this.width) {
                        currentRight = 0
                        currentBottom += marginTop + marginBottom + child.measuredHeight
                    } else {
                        currentRight += marginRight
                    }
                } else if (currentRight != 0 && currentRight + marginRight < width) {
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
            if (currentRight + btnAdd.measuredWidth > width) {
                currentRight = 0
                currentBottom += marginTop + marginBottom + btnAdd.measuredHeight
            } else {
                currentRight += marginRight
            }
            btnAdd.layout(
                currentRight,
                currentBottom,
                currentRight + btnAdd.measuredWidth,
                currentBottom + btnAdd.measuredHeight
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

}
