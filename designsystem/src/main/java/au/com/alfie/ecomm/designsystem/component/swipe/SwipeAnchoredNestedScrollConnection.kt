package au.com.alfie.ecomm.designsystem.component.swipe

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

@OptIn(ExperimentalFoundationApi::class)
internal val AnchoredDraggableState<SwipeAnchor>.SwipeAnchoredNestedScrollConnection: NestedScrollConnection
    get() = object : NestedScrollConnection {

        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y
            return if (delta < 0 && source == NestedScrollSource.Drag) {
                dispatchRawDelta(delta).toOffset()
            } else {
                Offset.Zero
            }
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            return if (source == NestedScrollSource.Drag) {
                dispatchRawDelta(available.y).toOffset()
            } else {
                Offset.Zero
            }
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            val toFling = available.y
            val currentOffset = requireOffset()
            val minAnchor = anchors.minAnchor()
            return if (toFling < 0 && currentOffset > minAnchor) {
                settle(toFling)
                available
            } else {
                Velocity.Zero
            }
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            settle(available.y)
            return available
        }

        private fun Float.toOffset(): Offset = Offset(x = 0f, y = this)
    }
