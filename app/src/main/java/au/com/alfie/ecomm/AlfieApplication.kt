package au.com.alfie.ecomm

import android.app.Application
import au.com.alfie.ecomm.core.commons.log.TimberConfigurator
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
