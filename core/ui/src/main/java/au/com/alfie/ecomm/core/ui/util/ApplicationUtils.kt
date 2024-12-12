package au.com.alfie.ecomm.core.ui.util

import android.app.Activity
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK

object ApplicationUtils {

    fun restartActivity(activity: Activity) {
        val landingIntent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
        landingIntent?.let {
            landingIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(landingIntent)
            activity.finish()
        }
    }
}
