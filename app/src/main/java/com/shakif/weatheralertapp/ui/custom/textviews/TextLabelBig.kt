package com.shakif.weatheralertapp.ui.custom.textviews

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import com.shakif.weatheralertapp.R

class TextLabelBig : TextLabel {
    constructor(context: Context) : this(context, null ,0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val outValue = TypedValue()
        resources.getValue(R.dimen.fontSizeBig, outValue, true)
        val fontSizeBig = outValue.float
        fontSize = fontSizeBig
        fontWeight = TextFontWeight.MEDIUM
    }
}