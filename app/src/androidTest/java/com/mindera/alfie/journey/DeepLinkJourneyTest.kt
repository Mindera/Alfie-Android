package com.mindera.alfie.journey

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindera.alfie.core.testseed.SeedAnchors
import com.mindera.alfie.core.ui.test.HOME_TAB
import com.mindera.alfie.core.ui.test.PDP_PRODUCT_NAME
import com.mindera.alfie.uitest.EnUsLocaleRule
import com.mindera.alfie.uitest.UI_TIMEOUT_MILLIS
import com.mindera.alfie.uitest.exists
import com.mindera.alfie.uitest.hasNonBlankText
import com.mindera.alfie.uitest.launchMainActivity
import com.mindera.alfie.uitest.waitForNode
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Deep link: launch on a product link → the PDP opens → back to Home → the activity is recreated
 * (as on a configuration change) → it stays on Home. `MainActivity` handles its launch link only on
 * a fresh start, so a recreation doesn't open the PDP again.
 */
@RunWith(AndroidJUnit4::class)
class DeepLinkJourneyTest {

    @get:Rule(order = 0)
    val locale = EnUsLocaleRule()

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    private val pdpName = hasTestTag(PDP_PRODUCT_NAME)

    @Test
    fun aProductLinkOpensThePdpOnlyOnce() {
        launchMainActivity(PRODUCT_LINK).use { scenario ->
            // Its loading placeholder is blank, so a non-blank name means the product loaded.
            composeRule.waitForNode(pdpName and hasNonBlankText(), useUnmergedTree = true)

            scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
            composeRule.waitUntil("the PDP to close", UI_TIMEOUT_MILLIS) {
                !composeRule.exists(pdpName, useUnmergedTree = true)
            }
            composeRule.waitForNode(hasTestTag(HOME_TAB))

            scenario.recreate()

            // The recreated activity would post the launch link's handling to the main looper from
            // onCreate. Once it shows a screen, waitForIdle drains that and the navigation it triggers.
            composeRule.waitUntil("the recreated activity to show a screen", UI_TIMEOUT_MILLIS) {
                composeRule.exists(hasTestTag(HOME_TAB)) || composeRule.exists(pdpName, useUnmergedTree = true)
            }
            composeRule.waitForIdle()
            assertFalse(
                "The recreated activity opened the launch link's PDP again",
                composeRule.exists(pdpName, useUnmergedTree = true)
            )
            assertTrue("The recreated activity isn't on Home", composeRule.exists(hasTestTag(HOME_TAB)))
        }
    }

    private companion object {
        // The host doesn't matter: DeeplinkSpec matches the path only. See ProductDetailsDeeplinks.
        val PRODUCT_LINK = "https://www.alfie.com/product/${SeedAnchors.Products.default}"
    }
}
