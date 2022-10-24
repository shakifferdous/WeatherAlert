package com.shakif.weatheralertapp.utility

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import com.shakif.weatheralertapp.R
import com.shakif.weatheralertapp.application.MainApplication.Companion.appResources

object Colors {
    val primaryColor = ResourcesCompat.getColor(appResources , R.color.primaryAccentColor, null)

    val primaryTextColor = ResourcesCompat.getColor(appResources, R.color.primaryTextColor, null)

    val primaryTextColorLight = ResourcesCompat.getColor(appResources, R.color.primaryTextColorLight, null)

    val secondaryTextColor = ResourcesCompat.getColor(appResources, R.color.secondaryTextColor, null)

    val darkBgTextColor = ResourcesCompat.getColor(appResources, R.color.white, null)

    val secondaryAccentColor = ResourcesCompat.getColor(appResources, R.color.secondaryAccentColor, null)

    val tertiaryAccentColor = ResourcesCompat.getColor(appResources, R.color.tertiaryAccentColor, null)

    val borderColor = ResourcesCompat.getColor(appResources, R.color.borderColor, null)

    val mainBackgroundColor = ResourcesCompat.getColor(appResources, R.color.mainBackgroundColor, null)

    val componentBackgroundColor = ResourcesCompat.getColor(appResources, R.color.componentBackgroundColor, null)

    val dropdownBGColor = ResourcesCompat.getColor(appResources, R.color.dropdownBGColor, null)

    val successColor = ResourcesCompat.getColor(appResources, R.color.successColor, null)

    val warningColor = ResourcesCompat.getColor(appResources, R.color.warningColor, null)

    val dangerColor = ResourcesCompat.getColor(appResources, R.color.dangerColor, null)

    val whiteColor = ResourcesCompat.getColor(appResources, R.color.white, null)

    val blackColor = ResourcesCompat.getColor(appResources, R.color.black, null)

    @ColorInt
    fun @receiver:androidx.annotation.ColorInt Int.darker(@FloatRange(from = 0.00, to = 1.00) percentage: Float): Int {
        return ColorUtils.blendARGB(this, Color.BLACK, percentage)
    }

    @ColorInt
    fun @receiver:androidx.annotation.ColorInt Int.lighter(@FloatRange(from = 0.0, to = 1.0) percentage: Float): Int {
        return ColorUtils.blendARGB(this, Color.WHITE, percentage)
    }
}