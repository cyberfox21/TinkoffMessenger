package com.cyberfox21.tinkoffmessanger.presentation

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.cyberfox21.tinkoffmessanger.R

class CustomEmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var text = ""

    var count = ""
        set(value) {
            field = value
            requestLayout()
        }

    var emoji = ""
        set(value) {
            field = value
            requestLayout()
        }

    private val textBoundsRectangle = Rect()
    private val textCoordinate = PointF()

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = resources.getDimension(R.dimen.btnAddHeight)
        textAlign = Paint.Align.CENTER
        setBackgroundResource(R.drawable.custom_emoji_view_bg)
    }

    var onEmojiClickListener: OnEmojiClickListener? = null

    interface OnEmojiClickListener {
        fun onEmojiClick(view: CustomEmojiView)
    }

    init {

        with(context.obtainStyledAttributes(attrs, R.styleable.CustomEmojiView)) {
            setPadding(
                EMOJI_VIEW_PADDING,
                EMOJI_VIEW_PADDING,
                EMOJI_VIEW_PADDING,
                EMOJI_VIEW_PADDING
            )
            recycle()
        }

        rootView.setOnClickListener {
            onEmojiClickListener?.onEmojiClick(it as CustomEmojiView)
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        text = "$emoji $count"

        textPaint.getTextBounds(text, 0, text.length, textBoundsRectangle)
        val textHeight = textBoundsRectangle.height()
        val textWidth = textBoundsRectangle.width()

        val textTotalWidth = textWidth + paddingRight + paddingLeft
        val textTotalHeight = textHeight + paddingTop + paddingBottom

        val resultWidth = resolveSize(textTotalWidth, widthMeasureSpec)

        val resultHeight = resolveSize(textTotalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        textCoordinate.x = (w / 2).toFloat()
        textCoordinate.y = h / 2f + textBoundsRectangle.height() / 3
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(text, textCoordinate.x, textCoordinate.y, textPaint)
    }


    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState =
            super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)

        if (isSelected) {
            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)
        }
        return drawableState
    }

    companion object {
        const val EMOJI_VIEW_PADDING = 20

        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
    }
}
