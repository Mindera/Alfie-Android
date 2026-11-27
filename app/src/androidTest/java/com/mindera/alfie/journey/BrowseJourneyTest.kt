package com.mindera.alfie.journey

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindera.alfie.core.ui.test.CATEGORY_ITEM
import com.mindera.alfie.core.ui.test.PDP_PRODUCT_NAME
import com.mindera.alfie.core.ui.test.PRODUCT_CARD
import com.mindera.alfie.core.ui.test.SHOP_TAB
import com.mindera.alfie.uitest.EnUsLocaleRule
import com.mindera.alfie.uitest.UI_TIMEOUT_MILLIS
import com.mindera.alfie.uitest.exists
import com.mindera.alfie.uitest.hasNonBlankText
import com.mindera.alfie.uitest.launchMainActivity
import com.mindera.alfie.uitest.waitForNode
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Browse: launch → Shop → the first category, down to a leaf → the first product → the PDP shows a
 * non-empty name. "The first" holds for any item: every category leads to a listing, and every
 * product has a name.
 */
@RunWith(AndroidJUnit4::class)
class BrowseJourneyTest {

    @get:Rule(order = 0)
    val locale = EnUsLocaleRule()

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    private val loadedCategoryRow = hasTestTag(CATEGORY_ITEM) and hasNonBlankText()

    // Loading placeholders carry the same tag, but aren't clickable until the product has loaded.
    private val loadedProductCard = hasTestTag(PRODUCT_CARD) and isEnabled()

    @Test
    fun browsingTheShopOpensAProductPage() {
        launchMainActivity().use {
            composeRule.waitForNode(hasTestTag(SHOP_TAB)).performClick()

            openFirstCategoryDownToALeaf()

            composeRule.waitForNode(loadedProductCard).performClick()

            // Its loading placeholder is blank, so a non-blank name means the product loaded.
            composeRule.waitForNode(hasTestTag(PDP_PRODUCT_NAME) and hasNonBlankText(), useUnmergedTree = true)
        }
    }

    /**
     * Taps the first category, and then the first sub-category, until a product listing opens. A
     * category with sub-categories opens another list; a leaf opens its listing.
     */
    private fun openFirstCategoryDownToALeaf() {
        repeat(MAX_CATEGORY_DEPTH) {
            val row = composeRule.waitForNode(loadedCategoryRow)
            val tappedRowId = row.fetchSemanticsNode().id
            row.performClick()

            // Once the tapped row has gone, so has its list: any row left is the next level's.
            composeRule.waitUntil("the tapped category row to go away", UI_TIMEOUT_MILLIS) {
                composeRule.onAllNodes(hasTestTag(CATEGORY_ITEM))
                    .fetchSemanticsNodes(atLeastOneRootRequired = false)
                    .none { it.id == tappedRowId }
            }
            composeRule.waitUntil("a product listing or the next category level", UI_TIMEOUT_MILLIS) {
                composeRule.exists(loadedProductCard) || composeRule.exists(loadedCategoryRow)
            }
            if (composeRule.exists(loadedProductCard)) return
        }
        fail("No product listing within $MAX_CATEGORY_DEPTH category levels")
    }

    private companion object {
        const val MAX_CATEGORY_DEPTH = 4
    }
}
