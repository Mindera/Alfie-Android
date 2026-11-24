package com.mindera.alfie.uitest

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onFirst

/** Long enough for a cold start and a Mock BFF round trip on a CI emulator. */
const val UI_TIMEOUT_MILLIS = 20_000L

/**
 * Waits until at least one node matches [matcher], then returns the first. Journeys wait for nodes
 * like this and never sleep.
 */
fun ComposeTestRule.waitForNode(
    matcher: SemanticsMatcher,
    useUnmergedTree: Boolean = false,
    timeoutMillis: Long = UI_TIMEOUT_MILLIS
): SemanticsNodeInteraction {
    waitUntil(conditionDescription = matcher.description, timeoutMillis = timeoutMillis) {
        exists(matcher, useUnmergedTree)
    }
    return onAllNodes(matcher, useUnmergedTree).onFirst()
}

/** Whether any node matches [matcher] now. Before the activity has set its content there are none. */
fun ComposeTestRule.exists(matcher: SemanticsMatcher, useUnmergedTree: Boolean = false): Boolean =
    onAllNodes(matcher, useUnmergedTree)
        .fetchSemanticsNodes(atLeastOneRootRequired = false)
        .isNotEmpty()

/** Text that isn't blank: a loaded row or name, not a shimmering placeholder. */
fun hasNonBlankText(): SemanticsMatcher = SemanticsMatcher("has non-blank text") { node ->
    node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.isNotBlank() }
}
