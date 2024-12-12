package au.com.alfie.ecomm.core.ui.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.SHARE_STATE_OFF

object CustomTabsUtils {

    @Throws(ActivityNotFoundException::class)
    fun launchUrl(
        context: Context,
        url: String,
        shareState: Int = SHARE_STATE_OFF,
        showTitle: Boolean = true
    ) {
        CustomTabsIntent.Builder()
            .setShareState(shareState)
            .setShowTitle(showTitle)
            .build()
            .launchUrl(context, Uri.parse(url))
    }
}
