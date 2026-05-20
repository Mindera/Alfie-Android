package com.mindera.alfie.designsystem.component.bottomcard

import androidx.compose.runtime.Stable
import com.mindera.alfie.designsystem.component.swipe.SwipeAnchor
import com.mindera.alfie.designsystem.component.swipe.SwipeAnchoredScope

@Stable
interface BottomCardScope {

    suspend fun snapTo(yOffset: Float)

    suspend fun peek()

    suspend fun reset()
}

@Stable
internal class BottomCardScopeInstance(
    var swipeAnchoredScope: SwipeAnchoredScope? = null
) : BottomCardScope {

    override suspend fun snapTo(yOffset: Float) {
        swipeAnchoredScope?.snapTo(yOffset)
    }

    override suspend fun peek() {
        swipeAnchoredScope?.animateTo(SwipeAnchor.Partial)
    }

    override suspend fun reset() {
        swipeAnchoredScope?.snapTo(SwipeAnchor.Start)
    }
}
