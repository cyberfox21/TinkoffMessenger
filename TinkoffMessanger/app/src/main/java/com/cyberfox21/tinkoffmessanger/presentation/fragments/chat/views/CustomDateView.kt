package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.cyberfox21.tinkoffmessanger.R

class CustomDateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var text = ""
        set(value) {
            field = value
            requestLayout()
        }

    private var marginVertical = DATE_VIEW_MARGIN_VERTICAL
        set(value) {
            field = value
            requestLayout()
        }

    private var marginHorizontal = DATE_VIEW_MARGIN_HORIZONTAL
        set(value) {
            field = value
            requestLayout()
        }

    private var padding = DATE_VIEW_PADDING
        set(value) {
            field = value
            requestLayout()
        }

    private val textBoundsRectangle = Rect()
    private val textCoordinate = PointF()

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = resources.getDimension(R.dimen.textSize14sp)
        textAlign = Paint.Align.CENTER
        setBackgroundResource(R.drawable.custom_emoji_view_bg)
    }

    init {
        with(context.obtainStyledAttributes(attrs, R.styleable.CustomDateView)) {
            setPadding(padding, padding, padding, padding)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        (layoutParams as ViewGroup.MarginLayoutParams).setMargins(
            marginHorizontal,
            marginVertical,
            marginHorizontal,
            marginVertical
        )
        textPaint.getTextBounds(text, 0, text.length, textBoundsRectangle)
        val textHeight = textBoundsRectangle.height()
        val textWidth = textBoundsRectangle.width()

        val textTotalWidth = textWidth + padding + padding
        val textTotalHeight = textHeight + padding + padding
        val resultWidth = resolveSize(textTotalWidth, widthMeasureSpec)

        val resultHeight = resolveSize(textTotalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        textCoordinate.x = (w / 2).toFloat()
        textCoordinate.y = (h / 2).toFloat() + textBoundsRectangle.height() / 2

    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(text, textCoordinate.x, textCoordinate.y, textPaint)
    }

    companion object {
        const val DATE_VIEW_PADDING = 30
        const val DATE_VIEW_MARGIN_HORIZONTAL = 150
        const val DATE_VIEW_MARGIN_VERTICAL = 20
    }
}