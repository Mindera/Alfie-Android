package au.com.alfie.ecomm.designsystem.component.modifier

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kotlinx.coroutines.launch

@Composable
fun Modifier.overflowNestedScroll(
    state: ScrollableState
): Modifier {
    val scope = rememberCoroutineScope()
    return this.nestedScroll(
        object : NestedScrollConnection {
            var isOverflowing by remember { mutableStateOf(true) }
            var previousAvailableOffset = Offset.Unspecified

            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                // Condition for initial state where offset is Unspecified so we can allow scroll to
                // overflow in the starting position of the list
                if (previousAvailableOffset == Offset.Unspecified) {
                    if (available.x > 0F) isOverflowing = true
                } else {
                    val isSourceDrag = source == NestedScrollSource.Drag
                    val isSourceFling = source == NestedScrollSource.Fling
                    val isForwardFromLimit = previousAvailableOffset.x == 0F && available.x < 0F
                    val isBackwardFromLimit = previousAvailableOffset.x == 0F && available.x > 0F
                    val isFromLimit = isForwardFromLimit || isBackwardFromLimit

                    // When dragging after reaching the limit (first or last item) from another position
                    // in the list, we want to disable overflowing scrolls once
                    // In case we get a fling gesture, since we are coming from the limit of the list,
                    // we want to enable overflowing in case a drag gesture is performed
                    when {
                        isSourceDrag && isFromLimit -> isOverflowing = false
                        isSourceFling && isFromLimit -> isOverflowing = true
                    }
                }

                return super.onPreScroll(available, source)
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val isSourceDrag = source == NestedScrollSource.Drag
                val hasReachedLimit = !state.canScrollForward || !state.canScrollBackward

                // While scrolling through the list, the first time we reach the start or the end and
                // we can't scroll more, we stop the scrolling gesture and enable overflow so the
                // scrolling is consumed by a parent
                if (isSourceDrag && !isOverflowing && hasReachedLimit) {
                    isOverflowing = true
                    scope.launch {
                        state.stopScroll(MutatePriority.PreventUserInput)
                    }
                }
                previousAvailableOffset = available
                return super.onPostScroll(consumed, available, source)
            }
        }
    )
}
