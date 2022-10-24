package com.shakif.weatheralertapp.ui.custom.textviews

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import com.shakif.weatheralertapp.R

class TextLabelSmall : TextLabel {
    constructor(context: Context) : this(context, null ,0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val outValue = TypedValue()
        resources.getValue(R.dimen.fontSizeSmall, outValue, true)
        val fontSizeSmall = outValue.float
        fontSize = fontSizeSmall
    }
}