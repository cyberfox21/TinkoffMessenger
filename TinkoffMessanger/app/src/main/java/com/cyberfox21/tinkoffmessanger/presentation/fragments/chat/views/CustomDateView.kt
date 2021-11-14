package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.cyberfox21.tinkoffmessanger.R

class CustomDateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var text = "1 Фев"
        set(value) {
            field = value
            requestLayout()
        }

    private val textBoundsRectangle = Rect()
    private val textCoordinate = PointF()

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = resources.getDimension(R.dimen.textSize14sp)
        textAlign = Paint.Align.LEFT
        setBackgroundResource(R.drawable.custom_emoji_view_bg)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(text, 0, text.length, textBoundsRectangle)
        val textHeight = textBoundsRectangle.height()
        val textWidth = textBoundsRectangle.width()

        val textTotalWidth = textWidth + paddingRight + paddingLeft
        val textTotalHeight = textHeight + paddingTop + paddingBottom

        val resultWidth = resolveSize(textTotalWidth, widthMeasureSpec)

        val resultHeight = resolveSize(textTotalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        textCoordinate.x = (left / 2).toFloat()
        textCoordinate.y = top / 2f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(text, textCoordinate.x, textCoordinate.y, textPaint)
    }
}