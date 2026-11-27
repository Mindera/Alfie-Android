package com.mindera.alfie.journey

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindera.alfie.core.testseed.SeedAnchors
import com.mindera.alfie.core.ui.test.PDP_PRODUCT_NAME
import com.mindera.alfie.core.ui.test.PDP_WISHLIST_BUTTON
import com.mindera.alfie.core.ui.test.PRODUCT_CARD
import com.mindera.alfie.core.ui.test.PRODUCT_WISHLIST_BUTTON
import com.mindera.alfie.core.ui.test.WISHLIST_CONFIRMATION
import com.mindera.alfie.core.ui.test.WISHLIST_EMPTY_STATE
import com.mindera.alfie.core.ui.test.WISHLIST_TAB
import com.mindera.alfie.uitest.EnUsLocaleRule
import com.mindera.alfie.uitest.UI_TIMEOUT_MILLIS
import com.mindera.alfie.uitest.exists
import com.mindera.alfie.uitest.hasNonBlankText
import com.mindera.alfie.uitest.launchMainActivity
import com.mindera.alfie.uitest.waitForNode
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Wishlist: launch on a product link → add it to the wishlist → back → the Wishlist tab shows one
 * item → remove it → the Wishlist empty state shows. The wishlist lives in Room, so it starts empty:
 * Orchestrator clears the app's data before every test.
 */
@RunWith(AndroidJUnit4::class)
class WishlistJourneyTest {

    @get:Rule(order = 0)
    val locale = EnUsLocaleRule()

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    private val pdpName = hasTestTag(PDP_PRODUCT_NAME)

    // Loading placeholders carry the same tag, but aren't clickable until the product has loaded.
    private val loadedProductCard = hasTestTag(PRODUCT_CARD) and isEnabled()

    @Test
    fun aSavedProductShowsOnTheWishlistUntilItIsRemoved() {
        launchMainActivity(PRODUCT_LINK).use { scenario ->
            // Its loading placeholder is blank, so a non-blank name means the product loaded. The
            // toggle does nothing until then.
            composeRule.waitForNode(pdpName and hasNonBlankText(), useUnmergedTree = true)

            composeRule.waitForNode(hasTestTag(PDP_WISHLIST_BUTTON)).performScrollTo().performClick()
            // The toast confirms the save has been written. Leaving the PDP before then would cancel
            // the write with its ViewModel.
            composeRule.waitForNode(hasTestTag(WISHLIST_CONFIRMATION))

            scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
            composeRule.waitUntil("the PDP to close", UI_TIMEOUT_MILLIS) {
                !composeRule.exists(pdpName, useUnmergedTree = true)
            }
            composeRule.waitForNode(hasTestTag(WISHLIST_TAB)).performClick()

            composeRule.waitForNode(loadedProductCard)
            assertEquals(
                "The Wishlist should show the one saved product",
                1,
                composeRule.onAllNodes(loadedProductCard).fetchSemanticsNodes().size
            )

            // On the Wishlist, the card's heart removes the product.
            composeRule.waitForNode(hasTestTag(PRODUCT_WISHLIST_BUTTON)).performClick()

            composeRule.waitForNode(hasTestTag(WISHLIST_EMPTY_STATE))
        }
    }

    private companion object {
        // The host doesn't matter: DeeplinkSpec matches the path only. See ProductDetailsDeeplinks.
        val PRODUCT_LINK = "https://www.alfie.com/product/${SeedAnchors.Products.default}"
    }
}
