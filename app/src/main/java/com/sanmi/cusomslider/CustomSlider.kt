package com.sanmi.cusomslider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CustomSlider @JvmOverloads constructor(context: Context,
                                             attrs: AttributeSet? = null,
                                             defStyleAttr: Int = R.attr.seekBarStyle,
                                             defStyleRes: Int = 0) : SeekBar(context, attrs, defStyleAttr, defStyleRes){

    private var colors: ArrayList<Int> = arrayListOf(
        Color.RED, Color.YELLOW,
        Color.BLUE)
    private val w = getPixelValueFromDP(16f) // Width of color swatch
    private val h = getPixelValueFromDP(16f) // Height of color swatch
    private val halfW = if (w >= 0) w / 2f else 1f
    private val halfH = if (h >= 0) h / 2f else 1f
    var w2 = 0
    private var h2 = 0
    private var halfW2 = 1
    private var halfH2 =  1
    private val paint = Paint()
    private var noColorDrawable: Drawable? = null
        set(value) {
            w2 = value?.intrinsicWidth ?: 0
            h2 = value?.intrinsicHeight ?: 0
            halfW2 =if (w2 >= 0) w2 / 2 else 1
            halfH2 =if (h2 >= 0) h2 / 2 else 1
            value?.setBounds(-halfW2, -halfH2, halfW2, halfH2)
            field = value
        }



    private fun getPixelValueFromDP(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            context.resources.displayMetrics)
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSlider)


        try {
            colors = typedArray.getTextArray(R.styleable.CustomSlider_colors).map {color ->
                Color.parseColor(color.toString())
            } as ArrayList<Int>
        }finally {
            typedArray.recycle()
        }

        colors.add(0,android.R.color.transparent)
        max = colors.size - 1
        progressBackgroundTintList = ContextCompat.getColorStateList(context,
            android.R.color.transparent)
        progressTintList = ContextCompat.getColorStateList(context,
            android.R.color.transparent)
        splitTrack = false
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + getPixelValueFromDP(16f).toInt())
        thumb = context.getDrawable(R.drawable.ic_thumb)
        noColorDrawable = context.getDrawable(R.drawable.ic_clear_black_24dp)

        setOnSeekBarChangeListener(object  : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listeners.forEach {
                    it(colors[progress])
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })



    }


    private var listeners: ArrayList<(Int) -> Unit> = arrayListOf()

    fun addListener(function: (Int) -> Unit) {
        listeners.add(function)
    }

    fun removeListener(){
        listeners.removeAll(listeners)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTickMarks(canvas)
    }


    private fun drawTickMarks(canvas: Canvas?) {
        canvas?.let {
            val count = colors.size
            val saveCount = canvas.save()
            canvas.translate(paddingLeft.toFloat(), (height / 2).toFloat() + getPixelValueFromDP(16f))
            if (count > 1) {
                val spacing = (width - paddingLeft - paddingRight) / (count - 1).toFloat()
                for (i in 0 until count) {
                    if (i == 0) {
                        noColorDrawable?.draw(canvas)
                    } else {
                        paint.color = colors[i]
                        canvas.drawRect(-halfW, -halfW,
                            halfW, halfH, paint)
                        //canvas.drawCircle(-halfW, halfH, halfW, paint)
                    }
                    canvas.translate(spacing, 0f)
                }
                canvas.restoreToCount(saveCount)
            }
        }
    }

}