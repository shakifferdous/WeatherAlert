package com.shakif.weatheralertapp.ui.base

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.shakif.weatheralertapp.R
import java.util.*
import kotlin.concurrent.schedule

abstract class BaseFragment : Fragment() {

    fun showErrorDialog(message: String) {
        activity?.let {
            val builder = AlertDialog.Builder(it, R.style.AlertDialog)
            builder.setMessage(message)
            builder.setNegativeButton(getString(R.string.close)) { _, _ -> }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(it, R.color.black))
            val msgTxt = alertDialog.findViewById<TextView>(android.R.id.message)
            msgTxt?.textSize = 14.0f
        }
    }

    fun toggleUIEventsListener(delay: Long = 300) {
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        Timer().schedule(delay) {
            activity?.runOnUiThread {
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

    fun View.hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun showKeyboard(activity: Activity) {
        val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}