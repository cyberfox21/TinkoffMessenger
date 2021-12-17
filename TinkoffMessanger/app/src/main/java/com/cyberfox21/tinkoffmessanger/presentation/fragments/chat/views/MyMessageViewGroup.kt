package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views

import android.content.Context
import android.content.res.TypedArray
import android.text.SpannableString
import android.text.Spanned
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.marginTop
import com.cyberfox21.tinkoffmessanger.R

class MyMessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var myMessage: MyMessage
    var flexBoxLayout: FlexBoxLayout

    var time: String = ""
        set(value) {
            field = value
            val myMessage = getChildAt(0) as MyMessage
            myMessage.time = value
        }

    var message: Spanned = SpannableString("")
        set(value) {
            field = value
            val myMessage = getChildAt(0) as MyMessage
            myMessage.message = value
        }

    init {
        inflate(context, R.layout.my_message_viewgroup, this).apply {

        }

        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.MyMessageViewGroup,
            defStyleAttr,
            defStyleRes
        )
        message =
            SpannableString(typedArray.getString(R.styleable.MyMessageViewGroup_message).orEmpty())
        time = typedArray.getString(R.styleable.MyMessageViewGroup_time).orEmpty()
        typedArray.recycle()
//
        myMessage = (getChildAt(0) as MyMessage).apply {
            this.time = time
            this.message = message
        }
        flexBoxLayout = getChildAt(1) as FlexBoxLayout
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        require(childCount == 2) { "Child count should be 2, but was $childCount" }

        val myMessage = getChildAt(0)
        val flexBoxLayout = getChildAt(1)

        var totalWidth = 0
        var totalHeight = 0

        measureChildWithMargins(
            myMessage,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )

        val messageMargin = (myMessage.layoutParams as MarginLayoutParams)

        totalWidth += myMessage.measuredWidth
        totalHeight += myMessage.measuredHeight + messageMargin.bottomMargin

        measureChildWithMargins(flexBoxLayout, widthMeasureSpec, 0, heightMeasureSpec, totalHeight)

        val flexBoxMargin = (flexBoxLayout.layoutParams as MarginLayoutParams)

        totalWidth = maxOf(totalWidth, flexBoxMargin.leftMargin + flexBoxLayout.measuredWidth)

        // должно быть if (flexBoxLayout.measuredHeight != 0) totalHeight += flexBoxLayout.topMargin + flexBoxLayout.measuredHeight
        if (flexBoxLayout.measuredHeight != 0) totalHeight += flexBoxLayout.marginTop + flexBoxLayout.measuredHeight

        val resultWidth = resolveSize(paddingLeft + totalWidth + paddingRight, widthMeasureSpec)
        val resultHeight = resolveSize(paddingTop + totalHeight + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val myMessage = getChildAt(0)
        val flexBoxLayout = getChildAt(1)

        val messageMargin = (myMessage.layoutParams as MarginLayoutParams)
        val flexBoxMargin = (flexBoxLayout.layoutParams as MarginLayoutParams)

        myMessage.layout(
            measuredWidth - paddingRight - myMessage.measuredWidth,
            paddingTop,
            measuredWidth - paddingRight,
            paddingTop + myMessage.measuredHeight
        )

        flexBoxLayout.layout(
            measuredWidth - paddingRight - flexBoxLayout.measuredWidth,
            myMessage.bottom + messageMargin.bottomMargin + flexBoxMargin.topMargin,
            measuredWidth - paddingRight,
            myMessage.bottom + messageMargin.bottomMargin +
                    flexBoxMargin.topMargin + flexBoxLayout.measuredHeight
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
