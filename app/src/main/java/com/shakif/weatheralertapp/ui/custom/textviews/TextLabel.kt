package com.shakif.weatheralertapp.ui.custom.textviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.shakif.weatheralertapp.R
import com.shakif.weatheralertapp.utility.Colors

open class TextLabel: AppCompatTextView {
    enum class TextFontWeight {
        REGULAR,
        MEDIUM,
        BOLD;

        companion object {
            fun getTypeface(weight: TextFontWeight): Typeface {
                return when (weight) {
                    REGULAR -> Typeface.create("sans-serif", Typeface.NORMAL)
                    BOLD -> Typeface.create("sans-serif", Typeface.BOLD)
                    MEDIUM -> Typeface.create("sans-serif-light", Typeface.BOLD)
                }
            }
        }
    }

    var fontSize: Float = 14f
        set(value) {
            field = value
            applyStyles()
        }

    @ColorInt
    var fontColor: Int = Colors.primaryTextColor
        set(value) {
            field = value
            applyStyles()
        }

    var fontWeight: TextFontWeight = TextFontWeight.REGULAR
        set(value) {
            field = value
            applyStyles()
        }

    constructor(context: Context) : this(context, null , 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val outValue = TypedValue()
        resources.getValue(R.dimen.fontSizeRegular, outValue, true)
        val fontSizeRegular = outValue.float
        fontSize = fontSizeRegular
    }

    private fun applyStyles() {
        setTextColor(fontColor)
        textSize = fontSize
        typeface = TextFontWeight.getTypeface(fontWeight)
    }
}