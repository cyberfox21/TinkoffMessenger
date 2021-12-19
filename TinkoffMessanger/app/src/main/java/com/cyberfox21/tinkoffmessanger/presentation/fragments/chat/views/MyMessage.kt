package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.cyberfox21.tinkoffmessanger.R

class MyMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var time: String = ""
        set(value) {
            field = value
            (getChildAt(0) as TextView).text = value
        }

    var message: Spanned = SpannableString("")
        set(value) {
            field = value
            (getChildAt(1) as TextView).text = value
        }

    private var isFullWidth: Boolean = false

    init {
        inflate(context, R.layout.my_message, this).apply {
            background =
                ResourcesCompat.getDrawable(resources, R.drawable.my_message_bg, context.theme)
        }
        (getChildAt(0) as AppCompatTextView).apply { text = time }
        (getChildAt(1) as AppCompatTextView).apply { text = message }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        require(childCount == 2) { "Child count should be 2, but was $childCount" }

        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight

        val tvTime = getChildAt(0)
        val tvMessage = getChildAt(1)

        var totalWidth = 0
        var totalHeight = 0

        measureChildWithMargins(tvMessage, widthMeasureSpec, 0, heightMeasureSpec, 0)

        val messageMargin = (tvMessage.layoutParams as MarginLayoutParams)

        totalWidth += tvMessage.measuredWidth + messageMargin.rightMargin
        totalHeight += tvMessage.measuredHeight

        measureChildWithMargins(tvTime, widthMeasureSpec, totalWidth, heightMeasureSpec, 0)

        val timeMargin = (tvTime.layoutParams as MarginLayoutParams)

        if (totalWidth + timeMargin.leftMargin + tvTime.measuredWidth >= width) {

            measureChildWithMargins(tvMessage, widthMeasureSpec, 0, heightMeasureSpec, 0)

            measureChildWithMargins(
                tvTime,
                widthMeasureSpec,
                0,
                heightMeasureSpec,
                totalHeight
            )

            isFullWidth = true

            totalWidth = maxOf(totalWidth, timeMargin.leftMargin + tvTime.measuredWidth)
            totalHeight += tvTime.measuredHeight + timeMargin.topMargin
        } else {
            isFullWidth = false

            totalWidth += timeMargin.leftMargin + tvTime.measuredWidth
            totalHeight = maxOf(totalHeight, tvTime.measuredHeight)
        }

        val resultWidth = resolveSize(paddingLeft + totalWidth + paddingRight, widthMeasureSpec)
        val resultHeight = resolveSize(paddingTop + totalHeight + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val tvTime = getChildAt(0)
        val tvMessage = getChildAt(1)


        val messageMargin = (tvMessage.layoutParams as MarginLayoutParams)
        val timeMargin = (tvTime.layoutParams as MarginLayoutParams)

        tvMessage.layout(
            paddingLeft,
            paddingTop,
            paddingLeft + tvMessage.measuredWidth,
            paddingTop + tvMessage.measuredHeight
        )

        if (isFullWidth) {
            tvTime.layout(
                measuredWidth - paddingRight - tvTime.measuredWidth,
                paddingTop + tvMessage.measuredHeight + timeMargin.topMargin,
                measuredWidth - paddingRight,
                paddingTop + tvMessage.measuredHeight + tvTime.measuredHeight + timeMargin.topMargin
            )
        } else {
            tvTime.layout(
                tvMessage.right + messageMargin.rightMargin + timeMargin.leftMargin,
                paddingTop,
                tvMessage.right + messageMargin.rightMargin + timeMargin.leftMargin + tvTime.measuredWidth,
                paddingTop + tvTime.measuredHeight
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
