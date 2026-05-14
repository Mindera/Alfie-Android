package com.mindera.alfie

import android.app.Application
import com.mindera.alfie.core.commons.log.TimberConfigurator
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AlfieApplication : Application() {

    @Inject
    lateinit var timberConfigurator: TimberConfigurator

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this.applicationContext)
        timberConfigurator.setup()
    }
}
