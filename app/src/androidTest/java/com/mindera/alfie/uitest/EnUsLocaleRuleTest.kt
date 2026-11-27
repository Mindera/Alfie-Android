package com.mindera.alfie.uitest

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

/** The pin holds whatever the device's locale is: run the UI suite with the AVD set to another one. */
@RunWith(AndroidJUnit4::class)
class EnUsLocaleRuleTest {

    @get:Rule
    val locale = EnUsLocaleRule()

    @Test
    fun theAppStartsInEnUs() {
        launchMainActivity().use { scenario ->
            scenario.onActivity { activity ->
                assertEquals(Locale.US, activity.resources.configuration.locales[0])
                assertEquals(Locale.US, Locale.getDefault())
                assertEquals("1,234.50", String.format("%,.2f", 1234.5))
            }
        }
    }
}
