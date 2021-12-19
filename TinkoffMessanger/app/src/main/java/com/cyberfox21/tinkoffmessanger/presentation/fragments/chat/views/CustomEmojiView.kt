package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.cyberfox21.tinkoffmessanger.R

class CustomEmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var count = 0
        set(value) {
            field = value
            requestLayout()
        }

    var emoji = ""
        set(value) {
            field = value
            requestLayout()
        }

    var countTextSize = 0f
        set(value) {
            field = value
            requestLayout()
        }
    var countTextColor = 0
        set(value) {
            field = value
            requestLayout()
        }

    private val textBoundsRectangle = Rect()
    private val emojiBoundsRectangle = Rect()

    private val textCoordinate = PointF()
    private val emojiCoordinate = PointF()

    private val textPaint = Paint().apply { ANTI_ALIAS_FLAG }

    init {

        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomEmojiView,
            defStyleAttr,
            defStyleRes
        )
        emoji = typedArray.getString(R.styleable.CustomEmojiView_emoji).orEmpty()
        count = typedArray.getInt(R.styleable.CustomEmojiView_count, DEFAULT_VALUE_COUNT)
        countTextSize = typedArray.getDimension(
            R.styleable.CustomEmojiView_countTextSize,
            DEFAULT_VALUE_COUNT_TEXT_SIZE
        )
        countTextColor = typedArray.getColor(
            R.styleable.CustomEmojiView_countTextColor,
            DEFAULT_VALUE_COUNT_TEXT_COLOR
        )
        typedArray.recycle()

        textPaint.textSize = countTextSize
        textPaint.color = countTextColor
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        textPaint.getTextBounds(emoji, 0, emoji.length, emojiBoundsRectangle)
        textPaint.getTextBounds(count.toString(), 0, count.toString().length, textBoundsRectangle)

        val textTotalWidth =
            paddingLeft + emojiBoundsRectangle.width() + textBoundsRectangle.width() + paddingRight
        val textTotalHeight = paddingTop + maxOf(
            emojiBoundsRectangle.height(),
            textBoundsRectangle.height()
        ) + paddingBottom

        val resultWidth = resolveSize(maxOf(textTotalWidth, layoutParams.width), widthMeasureSpec)

        val resultHeight =
            resolveSize(maxOf(textTotalHeight, layoutParams.height), heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        emojiCoordinate.x = w / 2f - emojiBoundsRectangle.width() / 2f
        emojiCoordinate.y =
            measuredHeight / 2f + emojiBoundsRectangle.height() / 2

        textCoordinate.x = w / 2f + emojiBoundsRectangle.width() / 2f
        textCoordinate.y = measuredHeight / 2f + textBoundsRectangle.height() / 2
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(emoji, emojiCoordinate.x, emojiCoordinate.y, textPaint)
        canvas.drawText(count.toString(), textCoordinate.x, textCoordinate.y, textPaint)
    }


    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState =
            super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)
        if (isSelected) mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)
        return drawableState
    }

    companion object {

        const val DEFAULT_VALUE_COUNT = 0
        const val DEFAULT_VALUE_COUNT_TEXT_SIZE = 0f
        const val DEFAULT_VALUE_COUNT_TEXT_COLOR = 0

        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
    }
}
