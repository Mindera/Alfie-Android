package com.mindera.alfie.designsystem.component.swipe

import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mindera.alfie.designsystem.animation.standardAccelerate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Hoisted state for a [SwipeActions] row, so the reveal can also be driven by a tap rather than only
 * by dragging — swipe-only actions are unreachable for switch-access and keyboard users.
 */
@Stable
@OptIn(ExperimentalFoundationApi::class)
class SwipeActionsState internal constructor(
    internal val draggableState: AnchoredDraggableState<SwipeActionsAnchor>,
    private val scope: CoroutineScope
) {

    /**
     * Whether the actions are showing. True while the row is still settling open as well as once it
     * has, so a tap part-way through the animation reverses it instead of starting another one.
     */
    val isOpen: Boolean
        get() = draggableState.currentValue == SwipeActionsAnchor.Open ||
            draggableState.targetValue == SwipeActionsAnchor.Open

    fun open() {
        scope.launch { draggableState.animateTo(SwipeActionsAnchor.Open) }
    }

    fun close() {
        scope.launch { draggableState.animateTo(SwipeActionsAnchor.Closed) }
    }

    fun toggle() {
        if (isOpen) close() else open()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberSwipeActionsState(): SwipeActionsState {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val decayAnimationSpec = remember(density) { splineBasedDecay<Float>(density) }
    return remember(density, scope) {
        SwipeActionsState(
            draggableState = AnchoredDraggableState(
                initialValue = SwipeActionsAnchor.Closed,
                snapAnimationSpec = standardAccelerate(),
                decayAnimationSpec = decayAnimationSpec,
                // Past halfway, or flung hard enough, the row settles to the far anchor.
                positionalThreshold = { distance -> distance / 2 },
                velocityThreshold = { with(density) { 125.dp.toPx() } }
            ),
            scope = scope
        )
    }
}
