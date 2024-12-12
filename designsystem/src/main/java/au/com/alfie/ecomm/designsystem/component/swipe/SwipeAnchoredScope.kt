package au.com.alfie.ecomm.designsystem.component.swipe

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.runtime.Immutable

@Immutable
interface SwipeAnchoredScope {

    suspend fun animateTo(swipeAnchor: SwipeAnchor)

    suspend fun snapTo(swipeAnchor: SwipeAnchor)

    suspend fun snapTo(yOffset: Float)
}

@Immutable
@OptIn(ExperimentalFoundationApi::class)
internal class SwipeAnchoredScopeInstance(
    private val anchoredDraggableState: AnchoredDraggableState<SwipeAnchor>
) : SwipeAnchoredScope {

    override suspend fun animateTo(swipeAnchor: SwipeAnchor) {
        anchoredDraggableState.animateTo(swipeAnchor)
    }

    override suspend fun snapTo(swipeAnchor: SwipeAnchor) {
        anchoredDraggableState.snapTo(swipeAnchor)
    }

    override suspend fun snapTo(yOffset: Float) {
        anchoredDraggableState.dispatchRawDelta(yOffset)
    }
}
