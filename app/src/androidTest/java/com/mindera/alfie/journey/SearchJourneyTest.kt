package com.mindera.alfie.journey

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindera.alfie.core.testseed.SeedAnchors
import com.mindera.alfie.core.ui.test.PDP_PRODUCT_NAME
import com.mindera.alfie.core.ui.test.PRODUCT_CARD
import com.mindera.alfie.core.ui.test.SEARCH_INPUT
import com.mindera.alfie.core.ui.test.SEARCH_NO_RESULTS_SCREEN
import com.mindera.alfie.uitest.EnUsLocaleRule
import com.mindera.alfie.uitest.hasNonBlankText
import com.mindera.alfie.uitest.launchMainActivity
import com.mindera.alfie.uitest.waitForNode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Search: launch → open search from Home → submit `SearchTerms.many` → the results listing → the
 * first result → the PDP shows a non-empty name. "The first" holds for any result: every product
 * has a name. Also: typing `SearchTerms.none` shows the no-results state.
 */
@RunWith(AndroidJUnit4::class)
class SearchJourneyTest {

    @get:Rule(order = 0)
    val locale = EnUsLocaleRule()

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    // Home's field is disabled until tapped; tapping opens the search overlay and focuses it.
    private val openSearchField = hasTestTag(SEARCH_INPUT) and isEnabled() and isFocused()

    // Loading placeholders carry the same tag, but aren't clickable until the product has loaded.
    private val loadedProductCard = hasTestTag(PRODUCT_CARD) and isEnabled()

    @Test
    fun searchingOpensAProductPage() {
        launchMainActivity().use {
            openSearch().apply {
                performTextInput(SeedAnchors.SearchTerms.many)
                performImeAction()
            }

            // Only the results listing has product cards: Home shows none, and the overlay tags its
            // product suggestions SEARCH_SUGGESTIONS_PRODUCT_ITEM, which replaces their PRODUCT_CARD.
            composeRule.waitForNode(loadedProductCard).performClick()

            // Its loading placeholder is blank, so a non-blank name means the product loaded.
            composeRule.waitForNode(hasTestTag(PDP_PRODUCT_NAME) and hasNonBlankText(), useUnmergedTree = true)
        }
    }

    @Test
    fun aTermWithNoMatchesShowsNoResults() {
        launchMainActivity().use {
            openSearch().performTextInput(SeedAnchors.SearchTerms.none)

            composeRule.waitForNode(hasTestTag(SEARCH_NO_RESULTS_SCREEN))
        }
    }

    private fun openSearch() = composeRule.waitForNode(hasTestTag(SEARCH_INPUT)).let { field ->
        field.performClick()
        composeRule.waitForNode(openSearchField)
    }
}
