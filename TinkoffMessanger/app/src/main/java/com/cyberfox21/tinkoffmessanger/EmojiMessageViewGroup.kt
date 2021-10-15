package com.cyberfox21.tinkoffmessanger

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class EmojiMessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    init {
        inflate(context, R.layout.emoji_message_viewgroup, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        require(childCount == 5) { "Child count should be 5, but was $childCount" }
        val imageView = getChildAt(0)
        val time = getChildAt(1)
        val title = getChildAt(2)
        val description = getChildAt(3)
        val flexBoxLayout = getChildAt(4)

        val marginBottom = (imageView.layoutParams as MarginLayoutParams).bottomMargin
        val marginRight = (imageView.layoutParams as MarginLayoutParams).marginEnd

        measureChildWithMargins(
            imageView,
            widthMeasureSpec,
            imageView.measuredWidth,
            heightMeasureSpec,
            0
        )
        measureChildWithMargins(time, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(title, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(description, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(flexBoxLayout, widthMeasureSpec, 0, heightMeasureSpec, 0)

        val totalWidth =
            (4 * marginRight) + imageView.measuredWidth +
                    description.measuredWidth + time.measuredWidth
        val totalHeight = maxOf(
            4 * marginBottom + title.measuredHeight +
                    description.measuredHeight + flexBoxLayout.measuredHeight,
            imageView.measuredHeight
        )

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
        val imageView = getChildAt(0)
        val time = getChildAt(1)
        val title = getChildAt(2)
        val description = getChildAt(3)
        val flexBoxLayout = getChildAt(4)

        val marginBottom = (imageView.layoutParams as MarginLayoutParams).bottomMargin
        val marginRight = (imageView.layoutParams as MarginLayoutParams).rightMargin

        imageView.layout(
            0 + paddingLeft + marginRight,
            0 + paddingTop + marginBottom,
            imageView.measuredWidth + paddingLeft + marginRight,
            imageView.measuredHeight + paddingTop + marginBottom
        )

        time.layout(
            width - time.measuredWidth - paddingRight - marginRight,
            0 + paddingBottom + marginBottom,
            width - paddingRight - marginRight,
            time.measuredHeight + paddingBottom + marginBottom
        )

        title.layout(
            imageView.measuredWidth + paddingRight + 2 * marginRight,
            0 + paddingBottom + marginBottom,
            width - time.width - marginRight,
            time.height + paddingBottom + marginBottom
        )

        description.layout(
            imageView.measuredWidth + paddingRight + 2 * marginRight,
            title.bottom + paddingBottom + marginBottom,
            width - paddingRight - marginRight,
            title.bottom + description.measuredHeight + paddingBottom + marginBottom
        )

        flexBoxLayout.layout(
            imageView.measuredWidth + 2 * marginRight + paddingRight,
            description.bottom + paddingBottom + marginBottom,
            width - paddingRight - marginRight,
            description.bottom + flexBoxLayout.measuredHeight + paddingBottom + marginBottom
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
