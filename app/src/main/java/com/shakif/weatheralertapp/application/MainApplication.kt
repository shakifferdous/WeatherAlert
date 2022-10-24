package com.shakif.weatheralertapp.application

import android.app.Application
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import com.shakif.weatheralertapp.realm.DBManager
import com.shakif.weatheralertapp.utility.AppLifecycle
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {
    companion object {
        lateinit var appResources: Resources
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        appResources = resources
        DBManager.init(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycle(this))
    }
}