package com.shakif.weatheralertapp.ui.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.shakif.weatheralertapp.ui.main.activities.MainActivity
import com.shakif.weatheralertapp.utility.Colors

abstract class BaseActivity: AppCompatActivity() {

    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun additionalCheckForRelaunch(): Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Colors.primaryColor
        savedInstanceState?.let {
            if (it.getBoolean(didActivityComeBack) && additionalCheckForRelaunch()) {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                )
                finish()
                overridePendingTransition(0, 0)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(didActivityComeBack, true)
        super.onSaveInstanceState(outState)
    }

    fun locationPermissionAvailable(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

     fun askLocationPermission() {
        if (locationPermissionAvailable().not()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showAlert(
                    "Location Permission Needed",
                    "In order to get you current location weather info, please grant location permission") {
                    requestLocationPermission()
                }
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST)
    }

    fun showAlert(title: String, message: String, completion: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                completion()
            }
            .create()
            .show()
    }

    companion object {
        const val didActivityComeBack = "DID_COME_BACK"
        const val LOCATION_REQUEST = 1993
    }
}