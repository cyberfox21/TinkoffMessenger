package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views

import android.content.Context
import android.content.res.TypedArray
import android.text.SpannableString
import android.text.Spanned
import android.util.AttributeSet
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cyberfox21.tinkoffmessanger.R
import de.hdodenhof.circleimageview.CircleImageView

class AlienMessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var flexBoxLayout: FlexBoxLayout
    var alienMessage: AlienMessage

    var avatar: String = ""
        set(value) {
            field = value
            val imageView = (getChildAt(0) as CircleImageView)
            Glide.with(this)
                .load(avatar)
                .into(imageView)
        }
    var name: String = ""
        set(value) {
            field = value
            val alienMessage = getChildAt(1) as AlienMessage
            alienMessage.name = value
        }
    var time: String = ""
        set(value) {
            field = value
            val alienMessage = getChildAt(1) as AlienMessage
            alienMessage.time = value
        }
    var message: Spanned = SpannableString("")
        set(value) {
            field = value
            val alienMessage = getChildAt(1) as AlienMessage
            alienMessage.message = value
        }

    init {
        inflate(context, R.layout.alien_message_viewgroup, this)
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.AlienMessageViewGroup,
            defStyleAttr,
            defStyleRes
        )
        avatar = typedArray.getString(R.styleable.AlienMessageViewGroup_avatar) ?: ""
        name = typedArray.getString(R.styleable.AlienMessageViewGroup_name).orEmpty()
        message = SpannableString(
            typedArray.getString(R.styleable.AlienMessageViewGroup_message).orEmpty()
        )
        time = typedArray.getString(R.styleable.AlienMessageViewGroup_time).orEmpty()
        typedArray.recycle()

        val imageView = (getChildAt(0) as CircleImageView)
        Glide.with(this)
            .load(avatar)
            .into(imageView)

        flexBoxLayout = getChildAt(2) as FlexBoxLayout
        alienMessage = (getChildAt(1) as AlienMessage).apply {
            this.name = name
            this.message = message
            this.time = time
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        require(childCount == 3) { "Child count should be 3, but was $childCount" }
        val imageView = getChildAt(0)
        val alienMessage = getChildAt(1)
        val flexBoxLayout = getChildAt(2)

        var totalWidth = 0
        var totalHeight = 0

        measureChildWithMargins(imageView, widthMeasureSpec, 0, heightMeasureSpec, 0)
        val imageViewMargin = (imageView.layoutParams as MarginLayoutParams)

        totalWidth = maxOf(totalWidth, imageView.measuredWidth + imageViewMargin.rightMargin)
        totalHeight = maxOf(
            totalHeight,
            imageViewMargin.topMargin + imageView.measuredHeight + imageViewMargin.bottomMargin
        )

        measureChildWithMargins(alienMessage, widthMeasureSpec, totalWidth, heightMeasureSpec, 0)
        val alienMessageMargin = (alienMessage.layoutParams as MarginLayoutParams)

        totalWidth += alienMessageMargin.leftMargin + alienMessage.measuredWidth
        totalHeight =
            maxOf(totalHeight, alienMessage.measuredHeight + alienMessageMargin.bottomMargin)

        measureChildWithMargins(
            flexBoxLayout,
            widthMeasureSpec,
            imageView.measuredWidth,
            heightMeasureSpec,
            totalHeight
        )
        val flexBoxLayoutMargin = (flexBoxLayout.layoutParams as MarginLayoutParams)

        totalWidth = maxOf(
            totalWidth,
            imageView.measuredWidth + imageViewMargin.rightMargin,
            widthMeasureSpec
        )
        if (flexBoxLayout.measuredHeight != 0) totalHeight += flexBoxLayoutMargin.topMargin + flexBoxLayout.measuredHeight

        val resultWidth = resolveSize(paddingRight + totalWidth + paddingLeft, widthMeasureSpec)
        val resultHeight = resolveSize(paddingTop + totalHeight + paddingBottom, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val imageView = getChildAt(0)
        val alienMessage = getChildAt(1)
        val flexBoxLayout = getChildAt(2)

        val imageViewMargin = (imageView.layoutParams as MarginLayoutParams)
        val alienMessageMargin = (alienMessage.layoutParams as MarginLayoutParams)
        val flexBoxLayoutMargin = (flexBoxLayout.layoutParams as MarginLayoutParams)

        imageView.layout(
            paddingLeft,
            paddingTop,
            paddingLeft + imageView.measuredWidth,
            paddingTop + imageView.measuredHeight
        )

        alienMessage.layout(
            imageView.right + imageViewMargin.rightMargin + alienMessageMargin.leftMargin,
            paddingTop,
            imageView.right + imageViewMargin.rightMargin + alienMessageMargin.leftMargin + alienMessage.measuredWidth,
            paddingTop + alienMessage.measuredHeight
        )

        flexBoxLayout.layout(
            alienMessage.left,
            alienMessage.bottom + alienMessageMargin.bottomMargin + flexBoxLayoutMargin.topMargin,
            alienMessage.left + flexBoxLayout.measuredWidth,
            alienMessage.bottom + alienMessageMargin.bottomMargin + flexBoxLayoutMargin.topMargin + flexBoxLayout.measuredHeight
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
