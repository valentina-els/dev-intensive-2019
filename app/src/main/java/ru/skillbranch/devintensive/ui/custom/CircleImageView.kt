package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.Color.parseColor
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import kotlinx.android.synthetic.main.activity_profile.view.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr){
    companion object{
        private const val BORDER_COLOR:Int = Color.WHITE
        private const val BORDER_WIDTH = 2F
    }

    private var cv_borderColor : Int = BORDER_COLOR
    private var cv_borderWidth : Float = BORDER_WIDTH
    private val maskPain = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val initialsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()
    private val borderRect = Rect()
    private var initials = ""
    private var initialsColor : Int = resources.getColor(R.color.colorAccent, null)
    private lateinit var resultBm : Bitmap
    private lateinit var maskBm : Bitmap
    private lateinit var srcBm : Bitmap

    init {
        if(attrs != null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            cv_borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, BORDER_COLOR)
            cv_borderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth,
                BORDER_WIDTH)
            a.recycle()
        }
        scaleType = ScaleType.CENTER_CROP
        setup()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(w == 0)return
        with(viewRect){
            left = 0
            top = 0
            right = w
            bottom = h
        }

        prepareBitmap(w, h)
    }

    fun getBorderColor():Int = cv_borderColor

    fun setBorderColor(hex: String){
        try {
            cv_borderColor = parseColor(hex)
        }catch (e : IllegalArgumentException){
            return
        }

        setup()
    }

    fun setBorderColor(@ColorRes colorId: Int){
        cv_borderColor = colorId
        setup()
    }

    @Dimension
    fun getBorderWidth():Int = cv_borderWidth.toInt()

    fun setBorderWidth(@Dimension dp: Int){
        cv_borderWidth = dp.toFloat()
        setup()
    }

    private fun setup(){
        with(maskPain){
            color = Color.RED
            style = Paint.Style.FILL
        }

        with(borderPaint){
            style = Paint.Style.STROKE
            strokeWidth = cv_borderWidth
            color = cv_borderColor
        }
    }

    private fun prepareBitmap(w:Int, h:Int){
        maskBm = Bitmap.createBitmap(w,h,Bitmap.Config.ALPHA_8)
        resultBm = maskBm.copy(Bitmap.Config.ARGB_8888, true)
        val maskCanvas = Canvas(maskBm)
        maskCanvas.drawOval(viewRect.toRectF(), maskPain)
        maskPain.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)


        val resultCanvas = Canvas(resultBm)
        resultCanvas.drawBitmap(maskBm, viewRect, viewRect, null)
        if(drawable != null) {
            srcBm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
            if(srcBm != null)
            resultCanvas.drawBitmap(srcBm, viewRect, viewRect, maskPain)
        }
    }

    private fun drawInitials(canvas: Canvas){
        initialsPaint.color = initialsColor
        canvas.drawOval(viewRect.toRectF(), initialsPaint)
        with(initialsPaint){
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = height * 0.33f
        }
        val offsetY: Float = (initialsPaint.descent() + initialsPaint.ascent())/2
        canvas.drawText(initials, viewRect.exactCenterX(), viewRect.exactCenterY()- offsetY, initialsPaint)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawBitmap(resultBm, viewRect, viewRect, null)
        if(canvas!=null){ //initials.length != 0 &&
            drawInitials(canvas)
        }
        //resize
        val half = (cv_borderWidth/2).toInt()
        borderRect.set(viewRect)
        borderRect.inset(half, half)
        canvas?.drawOval(borderRect.toRectF(), borderPaint)

    }

    public fun setInitials(text:String, color:Int){
        initials = text
        initialsColor = color
        invalidate()
    }

}

