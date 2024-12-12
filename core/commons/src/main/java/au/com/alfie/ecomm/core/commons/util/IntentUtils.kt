package au.com.alfie.ecomm.core.commons.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import timber.log.Timber

object IntentUtils {

    private const val TEXT_PLAIN = "text/plain"

    fun share(
        context: Context,
        text: String,
        title: String? = null
    ) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            title?.let {
                putExtra(Intent.EXTRA_TITLE, it)
            }
            type = TEXT_PLAIN
        }

        val shareIntent = Intent.createChooser(sendIntent, title)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    @Throws(ActivityNotFoundException::class)
    fun phoneDialer(context: Context, number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        context.startActivity(intent)
    }

    fun sendEmail(
        context: Context,
        email: String,
        subject: String,
        body: String,
        title: String
    ) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = TEXT_PLAIN
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        context.startActivity(Intent.createChooser(intent, title))
    }

    fun openAppStore(
        context: Context,
        appUrl: String,
        webUrl: String
    ) {
        val intent = Intent(Intent.ACTION_VIEW)

        try {
            // See: http://developer.android.com/distribute/tools/promote/linking.html
            intent.data = Uri.parse(appUrl)
            context.startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            Timber.e(exception)
            // If Google Play app is not installed, we will try to open a web browser
            intent.data = Uri.parse(webUrl)
            context.startActivity(intent)
        }
    }
}
