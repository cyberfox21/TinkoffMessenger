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

    var name = ""
        set(value) {
            field = value
            requestLayout()
        }

    var message = ""
        set(value) {
            field = value
            requestLayout()
        }

    var image = R.drawable.ic_launcher_background
        set(value) {
            field = value
            requestLayout()
        }

    var time = ""
        set(value) {
            field = value
            requestLayout()
        }

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

        measureChildWithMargins(imageView, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(time, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(title, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(description, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(flexBoxLayout, widthMeasureSpec, 0, heightMeasureSpec, 0)

        val totalWidth = imageView.measuredWidth + description.measuredWidth + time.measuredWidth
        val totalHeight = maxOf(
            title.measuredHeight + description.measuredHeight + flexBoxLayout.measuredHeight,
            imageView.measuredHeight
        )

        val resultWidth = resolveSize(totalWidth + paddingRight + paddingLeft, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight + paddingTop + paddingBottom, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val imageView = getChildAt(0)
        val time = getChildAt(1)
        val title = getChildAt(2)
        val description = getChildAt(3)
        val flexBoxLayout = getChildAt(4)

        imageView.layout(
            0 + paddingLeft,
            0 + paddingTop,
            imageView.measuredWidth + paddingLeft,
            imageView.measuredHeight + paddingTop
        )

        time.layout(
            width - time.measuredWidth - paddingRight,
            0 + paddingBottom,
            width,
            time.measuredHeight + paddingBottom
        )

        title.layout(
            imageView.width + paddingRight,
            0 + paddingBottom,
            width - time.width,
            title.measuredHeight + paddingBottom
        )

        description.layout(
            imageView.width + paddingRight,
            title.bottom + paddingBottom,
            width - time.width,
            title.bottom + description.measuredHeight + paddingBottom
        )

        flexBoxLayout.layout(
            imageView.width + paddingRight,
            description.bottom + paddingBottom,
            width - time.width,
            description.bottom + flexBoxLayout.measuredHeight + paddingBottom
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
