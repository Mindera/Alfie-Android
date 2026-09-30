package com.mindera.alfie.designsystem.component.swipe

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.mindera.alfie.core.ui.event.ClickEvent

/**
 * One panel revealed behind a [SwipeActions] row.
 *
 * @param contentDescription spoken label for the panel. Defaults to null, in which case [label] is
 * announced; supply it when the visible label is shorthand that needs expanding.
 */
@Immutable
data class SwipeAction(
    @DrawableRes val icon: Int,
    val label: String,
    val onClick: ClickEvent,
    val type: SwipeActionType = SwipeActionType.Neutral,
    val contentDescription: String? = null,
    val testTag: String = ""
)
