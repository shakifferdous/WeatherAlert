package com.shakif.weatheralertapp.utility

import android.content.Context
import android.util.TypedValue

class Converter {
    companion object {
        fun dpInFloat(context: Context, dp: Float): Float =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }
}