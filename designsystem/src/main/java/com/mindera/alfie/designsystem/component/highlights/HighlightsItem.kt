package com.mindera.alfie.designsystem.component.highlights

import androidx.compose.runtime.Immutable
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.media.image.ImageUI

/**
 * A single editorial slide of the [Highlights] component (Figma: "Highlights", node 4421:132068).
 *
 * @param image full-bleed background image (rendered at [com.mindera.alfie.designsystem.component.image.ratio.Ratio.RATIO3x4]).
 * @param title overlaid headline, styled with `typography.display.medium`.
 * @param actionText optional call-to-action link (e.g. "Explore Collection"); shown only when [onActionClick] is also set.
 * @param onActionClick invoked when the [actionText] link is tapped.
 */
@Immutable
data class HighlightsItem(
    val image: ImageUI,
    val title: StringResource,
    val actionText: StringResource? = null,
    val onActionClick: ClickEvent? = null
)
