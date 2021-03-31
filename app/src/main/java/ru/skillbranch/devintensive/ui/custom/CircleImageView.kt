package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.Color.parseColor
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.dpToPx
import ru.skillbranch.devintensive.extensions.pxToDp
import kotlin.properties.Delegates

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr){

    companion object{
        private const val BORDER_COLOR:Int = Color.WHITE
        private const val BORDER_WIDTH = 2
    }

    @ColorInt
    var cv_borderColor : Int = BORDER_COLOR
    @Px
    private var cv_borderWidth : Float = context.dpToPx(BORDER_WIDTH)
    private val maskPain = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val avatarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()
    private val borderRect = Rect()
    private lateinit var resultBm : Bitmap
    private lateinit var maskBm : Bitmap
    private lateinit var srcBm : Bitmap
    var width : Int? = null
    var height : Int? = null


    init {
        if(attrs != null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            cv_borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, BORDER_COLOR)
            cv_borderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth,
                context.dpToPx(BORDER_WIDTH))
//            initials = a.getString(R.styleable.CircleImageView_initials) ?: ""
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

        width = w
        height = h

        update()

//        prepareShader(w,h)
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
        cv_borderColor = ContextCompat.getColor(App.applicationContext(), colorId)
        setup()
    }

    @Dimension
    fun getBorderWidth():Int = context.pxToDp(cv_borderWidth)

    fun setBorderWidth(@Dimension dp: Int){
        cv_borderWidth = context.dpToPx(dp)
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

        invalidate()
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
            resultCanvas.drawBitmap(srcBm, viewRect, viewRect, maskPain)
        }
    }

//    private fun drawInitials(canvas: Canvas){
//        initialsPaint.color = initialsColor
//        canvas.drawOval(viewRect.toRectF(), initialsPaint)
//        with(initialsPaint){
//            color = Color.WHITE
//            textAlign = Paint.Align.CENTER
//            textSize = height * 0.33f
//        }
//        val offsetY: Float = (initialsPaint.descent() + initialsPaint.ascent())/2
//        canvas.drawText(initials, viewRect.exactCenterX(), viewRect.exactCenterY()- offsetY, initialsPaint)
//    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawBitmap(resultBm, viewRect, viewRect, null)
//        if(canvas!=null){ //initials.length != 0 &&
//            drawInitials(canvas)
//        }


        println("DRAAAAAWWWWWWWWWWWWWWWWWWWWWWWHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH $drawable")

        if(drawable != null)
            canvas.drawOval(viewRect.toRectF(), avatarPaint)


        //resize
        val half = (cv_borderWidth/2).toInt()
        borderRect.set(viewRect)
        borderRect.inset(half, half)
        canvas.drawOval(borderRect.toRectF(), borderPaint)

    }


    private fun drawableToBitmap(drawable: Drawable?): Bitmap? =
        when (drawable) {
            null -> null
            is BitmapDrawable -> drawable.bitmap
            else -> try {
                val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    fun update(){
        if(width == 0 && drawable != null) return
        if(width != null && height != null) {
            val srcBm: Bitmap = drawable.toBitmap(width!!, height!!, Bitmap.Config.ARGB_8888)
            avatarPaint.shader = BitmapShader(srcBm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            invalidate()
        }
    }

    fun setInitials(text:String, color:Int){
//        imgInitials.initials = text
//        imgInitials.initialsColor = color
//        imgInitials.invalidateSelf()

//        this.srcBm
//        initials = text
//        initialsColor = color
//        invalidate()
    }

    class TextIconDrawable : Drawable(){

        private val viewRect: Rect = Rect()
        private val initialsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val height : Int = viewRect.height()

        var initials by Delegates.observable("") { _, _, _ -> invalidateSelf() }
        var initialsColor by Delegates.observable(Color.BLACK) { _, _, _ -> invalidateSelf() }

        override fun draw(canvas: Canvas) {
            initialsPaint.color = initialsColor
            canvas.drawOval(viewRect.toRectF(), initialsPaint)
            with(initialsPaint){
                color = Color.WHITE
                textAlign = Paint.Align.CENTER
                textSize = canvas.height * 0.33f
            }
            val offsetY: Float = (initialsPaint.descent() + initialsPaint.ascent())/2
            canvas.drawText(initials, viewRect.exactCenterX(), viewRect.exactCenterY()- offsetY, initialsPaint)
        }

        override fun setAlpha(p0: Int) {
            TODO("Not yet implemented")
        }

        override fun setColorFilter(p0: ColorFilter?) {
            TODO("Not yet implemented")
        }

        override fun getOpacity(): Int {
            TODO("Not yet implemented")
        }
    }
}

