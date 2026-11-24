package com.mindera.alfie.uitest

import android.app.LocaleManager
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.ExternalResource
import java.util.Locale

/**
 * Pins the `en_US` locale for a test, whatever the device's locale is: `formatMoney`'s
 * `String.format` follows the default locale, and resources follow the configuration's.
 *
 * It sets both before the test body runs, so it must apply before the activity starts; launch the
 * app from the test body with [launchMainActivity]. Both are restored afterwards.
 */
class EnUsLocaleRule : ExternalResource() {

    private lateinit var previousDefault: Locale
    private var previousAppLocales: LocaleList? = null

    override fun before() {
        previousDefault = Locale.getDefault()
        Locale.setDefault(Locale.US)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // The per-app locale is the configuration every activity of the app starts with.
            previousAppLocales = localeManager().applicationLocales
            localeManager().applicationLocales = LocaleList(Locale.US)
        } else {
            updateAppConfiguration(Locale.US)
        }
    }

    override fun after() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            previousAppLocales?.let { localeManager().applicationLocales = it }
        } else {
            updateAppConfiguration(previousDefault)
        }
        Locale.setDefault(previousDefault)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun localeManager(): LocaleManager =
        targetContext().getSystemService(LocaleManager::class.java)

    @Suppress("DEPRECATION") // Before API 33 there's no per-app locale to set instead.
    private fun updateAppConfiguration(locale: Locale) {
        val resources = targetContext().resources
        val configuration = Configuration(resources.configuration).apply { setLocale(locale) }
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun targetContext() = InstrumentationRegistry.getInstrumentation().targetContext
}
