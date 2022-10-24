package com.shakif.weatheralertapp.ui.custom.buttons

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.ColorUtils
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.shakif.weatheralertapp.R
import com.shakif.weatheralertapp.utility.Colors
import com.shakif.weatheralertapp.utility.Colors.darker
import com.shakif.weatheralertapp.utility.disableView
import com.shakif.weatheralertapp.utility.enableView

open class SubmitButton : AppCompatTextView, View.OnTouchListener {
    private var currentBgColor = Colors.primaryColor
    private val animator = ValueAnimator()
    private lateinit var shapeDrawable : MaterialShapeDrawable
    constructor(context: Context) : this(context, null ,0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        applySubmitButtonStyles()
    }

    private fun applySubmitButtonStyles() {
        setWillNotDraw(false)
        shapeDrawable = getBackgroundDrawable()
        setOnTouchListener(this)
        updateBackground()
        val outValue = TypedValue()
        resources.getValue(R.dimen.fontSizeRegular, outValue, true)
        val fontSizeRegular = outValue.float
        textSize = fontSizeRegular
        setTextColor(Colors.whiteColor)
        gravity = Gravity.CENTER
    }

    fun applySecondaryButtonStyles() {
        setWillNotDraw(false)
        setOnTouchListener(this)
        currentBgColor = Colors.componentBackgroundColor
        updateBackground(bgColor = Colors.componentBackgroundColor, borderColor = Colors.borderColor)
        val outValue = TypedValue()
        resources.getValue(R.dimen.fontSizeRegular, outValue, true)
        val fontSizeRegular = outValue.float
        textSize = fontSizeRegular
        setTextColor(Colors.primaryTextColor)
        gravity = Gravity.CENTER
    }

    private fun submitButtonOnTouch(view: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> darker(0.2f)
            MotionEvent.ACTION_MOVE -> drag()
            else -> retrieveColor()
        }
        return false
    }

    fun secondaryButtonOnTouch(view: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> darker(percentage = 0f, color = Colors.componentBackgroundColor, borderColor = Colors.borderColor)
            MotionEvent.ACTION_MOVE -> drag(borderColor = Colors.borderColor)
            else -> retrieveColor(Colors.componentBackgroundColor, borderColor = Colors.borderColor)
        }
        return false
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return submitButtonOnTouch(view, event)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun convertToSecondary() {
        currentBgColor = Colors.componentBackgroundColor
        updateBackground(bgColor = Colors.componentBackgroundColor, borderColor = Colors.borderColor)
        val outValue = TypedValue()
        resources.getValue(R.dimen.fontSizeRegular, outValue, true)
        val fontSizeRegular = outValue.float
        textSize = fontSizeRegular
        setTextColor(Colors.primaryTextColor)
        gravity = Gravity.CENTER
        setOnTouchListener { view, event -> return@setOnTouchListener secondaryButtonOnTouch(view, event) }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun convertToSubmitButton() {
        shapeDrawable = getBackgroundDrawable()
        updateBackground()
        val outValue = TypedValue()
        resources.getValue(R.dimen.fontSizeRegular, outValue, true)
        val fontSizeRegular = outValue.float
        textSize = fontSizeRegular
        setTextColor(Colors.primaryColor)
        gravity = Gravity.CENTER
        setOnTouchListener { view, event -> return@setOnTouchListener submitButtonOnTouch(view, event) }
    }

    fun disable() {
        this.disableView()
    }

    fun enable() {
        this.enableView()
    }

    private fun animateColorChanges(bgColor: Int, duration: Long = 300) {
        animator.setIntValues(currentBgColor, bgColor)
        animator.setEvaluator(ArgbEvaluator())
        animator.duration = duration
        animator.addUpdateListener {
            currentBgColor = it.animatedValue as Int
            shapeDrawable.fillColor = ColorStateList(arrayOf(
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_enabled)
            ), intArrayOf(it.animatedValue as Int, it.animatedValue as Int))
        }
        background = shapeDrawable
        animator.start()
    }

    private fun getBackgroundDrawable(): MaterialShapeDrawable {
        val outValue = TypedValue()
        resources.getValue(R.dimen.cornerRadius, outValue, true)
        val cornerRadius = outValue.float
        val shape = ShapeAppearanceModel().toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, cornerRadius)
            .build()

        return MaterialShapeDrawable(shape)
    }

    private fun updateBackground(bgColor: Int = Colors.primaryColor, borderColor: Int? = null) {
        borderColor?.let { color ->
            shapeDrawable.setStroke(resources.getDimension(R.dimen.borderWidth), color)
        } ?: kotlin.run { shapeDrawable.setStroke(resources.getDimension(R.dimen.borderWidth), Color.TRANSPARENT) }
        if (currentBgColor != bgColor) {
            animateColorChanges(bgColor, 30)
        } else {
            shapeDrawable.fillColor = ColorStateList(arrayOf(
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_enabled)
            ), intArrayOf(bgColor, bgColor))
            background = shapeDrawable
        }
    }

    private fun drag(borderColor: Int? = null) {
        borderColor?.let { color ->
            shapeDrawable.setStroke(resources.getDimension(R.dimen.borderWidth), color.darker(0.2f))
        } ?: kotlin.run { shapeDrawable.setStroke(resources.getDimension(R.dimen.borderWidth), Color.TRANSPARENT) }
        shapeDrawable.fillColor = ColorStateList(arrayOf(
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(-android.R.attr.state_enabled)
        ), intArrayOf(currentBgColor, currentBgColor))
        background = shapeDrawable
    }

    private fun lighter(percentage: Float, color: Int = Colors.primaryColor, borderColor: Int? = null) {
        val updatedColor = ColorUtils.blendARGB(color, Color.WHITE, percentage)
        updateBackground(bgColor = updatedColor, borderColor = borderColor)
    }

    private fun darker(percentage: Float, color: Int = Colors.primaryColor, borderColor: Int? = null) {
        val updatedColor = color.darker(percentage)
        val updatedBorderColor = borderColor?.darker(0.2f)
        updateBackground(bgColor = updatedColor, borderColor = updatedBorderColor)
    }

    private fun retrieveColor(color: Int = Colors.primaryColor, borderColor: Int? = null) {
        borderColor?.let {
            shapeDrawable.setStroke(resources.getDimension(R.dimen.borderWidth), it)
        } ?: kotlin.run { shapeDrawable.setStroke(resources.getDimension(R.dimen.borderWidth), Color.TRANSPARENT) }
        if (currentBgColor != color) {
            animateColorChanges(color)
        } else {
            shapeDrawable.fillColor = ColorStateList(arrayOf(
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_enabled)
            ), intArrayOf(color, color))
            background = shapeDrawable
        }
    }
}