package ru.skillbranch.devintensive.ui.custom

import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import androidx.core.graphics.ColorUtils
import kotlin.properties.Delegates

class TextIconDrawable: Drawable() {

    private var textPaint = TextPaint().apply {
        textAlign = Paint.Align.CENTER
    }
    var text by Delegates.observable("") { _, _, _ -> invalidateSelf() }
    var textColor by Delegates.observable(Color.WHITE) { _, _, _ -> invalidateSelf() }
    var background by Delegates.observable(Color.BLUE){ _, _, _ -> invalidateSelf() }

    private fun fitText(height: Int) {
        textPaint.textSize = 35f
        val widthAt35 = textPaint.measureText(text)
        textPaint.textSize = widthAt35 * 4f//height.toFloat()
        textPaint.textSize
    }


    override fun draw(canvas: Canvas) {
        val width = bounds.width()
        val height = bounds.height()
        fitText(height)
        textPaint.color = ColorUtils.setAlphaComponent(textColor, alpha)
        canvas.drawColor(background)
        val offsetY: Float = (textPaint.descent() + textPaint.ascent())/2
        canvas.drawText(text, width / 2f, height / 2f - offsetY, textPaint)
    }

    override fun setAlpha(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        textPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

}