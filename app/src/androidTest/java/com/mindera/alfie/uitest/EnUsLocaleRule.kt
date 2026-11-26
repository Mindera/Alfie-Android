package com.mindera.alfie.uitest

import android.app.LocaleManager
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
 *
 * It needs API 33+, for the per-app locale every activity of the app starts with. Below that it fails
 * the test rather than skipping it: `scripts/run-bff-tests.sh` reads a skipped test as a lost BFF.
 */
class EnUsLocaleRule : ExternalResource() {

    private lateinit var previousDefault: Locale
    private var previousAppLocales: LocaleList? = null

    override fun before() {
        check(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            "EnUsLocaleRule needs API 33+ (per-app locales), and this device runs API ${Build.VERSION.SDK_INT}. " +
                "Run on the alfie-bff-tests AVD (API 34)."
        }
        previousDefault = Locale.getDefault()
        Locale.setDefault(Locale.US)
        previousAppLocales = localeManager().applicationLocales
        localeManager().applicationLocales = LocaleList(Locale.US)
    }

    override fun after() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        previousAppLocales?.let { localeManager().applicationLocales = it }
        Locale.setDefault(previousDefault)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun localeManager(): LocaleManager =
        InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(LocaleManager::class.java)
}
