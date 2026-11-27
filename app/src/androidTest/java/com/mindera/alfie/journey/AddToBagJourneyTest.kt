package com.mindera.alfie.journey

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindera.alfie.MainActivity
import com.mindera.alfie.core.testseed.SeedAnchors
import com.mindera.alfie.core.ui.test.ADD_TO_BAG_CONFIRMATION
import com.mindera.alfie.core.ui.test.BAG_EMPTY_STATE
import com.mindera.alfie.core.ui.test.BAG_LINE
import com.mindera.alfie.core.ui.test.BAG_LINE_REMOVE_ACTION
import com.mindera.alfie.core.ui.test.BAG_TAB
import com.mindera.alfie.core.ui.test.PDP_ADD_TO_BAG_BUTTON
import com.mindera.alfie.core.ui.test.PDP_PRODUCT_NAME
import com.mindera.alfie.core.ui.test.PRODUCT_CARD
import com.mindera.alfie.core.ui.test.PRODUCT_NAME
import com.mindera.alfie.core.ui.test.REMOVE_CONFIRMATION
import com.mindera.alfie.uitest.EnUsLocaleRule
import com.mindera.alfie.uitest.UI_TIMEOUT_MILLIS
import com.mindera.alfie.uitest.exists
import com.mindera.alfie.uitest.hasNonBlankText
import com.mindera.alfie.uitest.launchMainActivity
import com.mindera.alfie.uitest.waitForNode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Add to bag, against the Mock BFF's Cart: launch on a link to the barcode product's always-in-stock
 * variant → Add to Bag → the confirmation → the Bag shows one line → open the line → its PDP → back
 * → swipe the line to Remove → the Bag's empty state.
 *
 * The Bag reads the BFF Cart, so the line only shows once the BFF has created the Cart with it, and
 * the remove confirmation only once the BFF has removed it. Each test starts with no Cart: the
 * Cart id lives in the app's DataStore, which the orchestrator's `clearPackageData` wipes.
 */
@RunWith(AndroidJUnit4::class)
class AddToBagJourneyTest {

    @get:Rule(order = 0)
    val locale = EnUsLocaleRule()

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    // Its loading placeholder is blank, so a non-blank name means the product loaded.
    private val loadedPdpName = hasTestTag(PDP_PRODUCT_NAME) and hasNonBlankText()
    private val bagLine = hasTestTag(BAG_LINE)

    @Test
    fun aVariantAddedToTheBagCanBeOpenedAndRemoved() {
        launchMainActivity(VARIANT_LINK).use { scenario ->
            addTheLinkedVariantToTheBag()
            leaveThePdp(scenario)

            composeRule.waitForNode(hasTestTag(BAG_TAB)).performClick()
            composeRule.waitForNode(bagLine)
            composeRule.onAllNodes(bagLine).assertCountEquals(1)

            openTheLineAndComeBack(scenario)

            removeTheLine()
            composeRule.waitForNode(hasTestTag(BAG_EMPTY_STATE))
            composeRule.waitForNode(hasTestTag(REMOVE_CONFIRMATION))
            composeRule.onAllNodes(bagLine).assertCountEquals(0)
        }
    }

    private fun addTheLinkedVariantToTheBag() {
        composeRule.waitForNode(loadedPdpName, useUnmergedTree = true)
        // Enabled only once a size is selected: the link's variant preselects one.
        composeRule.waitForNode(hasTestTag(PDP_ADD_TO_BAG_BUTTON) and isEnabled())
            .performScrollTo()
            .performClick()
        composeRule.waitForNode(hasTestTag(ADD_TO_BAG_CONFIRMATION))
    }

    /** The line opens its own product: the PDP shows the name the line shows. */
    private fun openTheLineAndComeBack(scenario: ActivityScenario<MainActivity>) {
        val lineCard = hasTestTag(PRODUCT_CARD) and hasAnyAncestor(bagLine)
        val lineName = composeRule.waitForNode(
            hasTestTag(PRODUCT_NAME) and hasAnyAncestor(bagLine) and hasNonBlankText(),
            useUnmergedTree = true
        ).fetchSemanticsNode().config.getOrNull(SemanticsProperties.Text).orEmpty().joinToString("")

        composeRule.waitForNode(lineCard).performClick()
        composeRule.waitForNode(hasTestTag(PDP_PRODUCT_NAME) and hasText(lineName), useUnmergedTree = true)

        leaveThePdp(scenario)
        composeRule.waitForNode(bagLine)
    }

    private fun removeTheLine() {
        // Swipe within the row, clear of the screen edges and the system back gesture there.
        composeRule.waitForNode(bagLine).performTouchInput {
            swipeLeft(startX = right * SWIPE_START, endX = right * SWIPE_END)
        }
        composeRule.waitForNode(hasTestTag(BAG_LINE_REMOVE_ACTION) and isEnabled()).performClick()
    }

    private fun leaveThePdp(scenario: ActivityScenario<MainActivity>) {
        scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
        composeRule.waitUntil("the PDP to close", UI_TIMEOUT_MILLIS) {
            !composeRule.exists(hasTestTag(PDP_PRODUCT_NAME), useUnmergedTree = true)
        }
    }

    private companion object {
        // The host doesn't matter: DeeplinkSpec matches the path only. See ProductDetailsDeeplinks.
        val VARIANT_LINK = "https://www.alfie.com/product/${SeedAnchors.Barcode.productSlug}" +
            "?variantId=${SeedAnchors.Barcode.variantId}"

        const val SWIPE_START = 0.9f
        const val SWIPE_END = 0.1f
    }
}
