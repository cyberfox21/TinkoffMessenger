package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.cyberfox21.tinkoffmessanger.R

class AlienMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var name: String = ""
        set(value) {
            field = value
            (getChildAt(0) as TextView).text = value
        }
    var time: String = ""
        set(value) {
            field = value
            (getChildAt(1) as TextView).text = value
        }
    var message: Spanned = SpannableString("")
        set(value) {
            field = value
            (getChildAt(2) as TextView).text = value
        }

    init {
        inflate(context, R.layout.alien_message, this)
        val tvName = getChildAt(0) as TextView
        val tvTime = getChildAt(1) as TextView
        val tvMessage = getChildAt(2) as TextView

        tvName.text = name
        tvTime.text = time
        tvMessage.text = message
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        require(childCount == 3) { "Child count should be 3, but was $childCount" }

        val name = getChildAt(0)
        val time = getChildAt(1)
        val description = getChildAt(2)

        var totalWidth = 0
        var totalHeight = 0

        measureChildWithMargins(name, widthMeasureSpec, 0, heightMeasureSpec, 0)
        val nameMargin = (name.layoutParams as MarginLayoutParams)

        totalWidth += name.measuredWidth + nameMargin.rightMargin
        totalHeight += name.measuredHeight + nameMargin.bottomMargin

        measureChildWithMargins(time, widthMeasureSpec, totalWidth, heightMeasureSpec, 0)
        val timeMargin = (time.layoutParams as MarginLayoutParams)

        totalWidth += timeMargin.leftMargin + time.measuredWidth
        totalHeight = maxOf(
            totalHeight,
            time.measuredHeight + maxOf(nameMargin.bottomMargin, timeMargin.bottomMargin)
        )

        measureChildWithMargins(description, widthMeasureSpec, 0, heightMeasureSpec, 0)
        val descriptionMargin = (description.layoutParams as MarginLayoutParams)

        totalWidth = maxOf(totalWidth, description.measuredWidth)
        totalHeight += description.measuredHeight + descriptionMargin.topMargin

        val resultWidth = resolveSize(paddingLeft + totalWidth + paddingRight, widthMeasureSpec)
        val resultHeight = resolveSize(paddingTop + totalHeight + paddingBottom, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val name = getChildAt(0)
        val time = getChildAt(1)
        val description = getChildAt(2)

        val nameMargin = (name.layoutParams as MarginLayoutParams)
        val timeMargin = (time.layoutParams as MarginLayoutParams)
        val descriptionMargin = (description.layoutParams as MarginLayoutParams)

        name.layout(
            paddingLeft,
            paddingTop,
            paddingLeft + name.measuredWidth,
            paddingTop + name.measuredHeight
        )

        time.layout(
            name.right + nameMargin.rightMargin + timeMargin.leftMargin,
            paddingTop,
            name.right + nameMargin.rightMargin + timeMargin.leftMargin + time.measuredWidth,
            paddingTop + time.measuredHeight
        )

        description.layout(
            paddingLeft + descriptionMargin.leftMargin,
            maxOf(name.bottom, time.bottom) + descriptionMargin.topMargin,
            paddingLeft + descriptionMargin.leftMargin + description.measuredWidth,
            maxOf(name.bottom, time.bottom) +
                    maxOf(nameMargin.bottomMargin, timeMargin.bottomMargin) +
                    descriptionMargin.topMargin +
                    description.measuredHeight
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
