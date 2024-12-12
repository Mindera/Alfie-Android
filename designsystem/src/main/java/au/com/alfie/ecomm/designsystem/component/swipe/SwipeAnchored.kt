package au.com.alfie.ecomm.designsystem.component.swipe

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.animation.standardAccelerate
import kotlin.math.roundToInt

private const val FIFTY_PERCENT = .5F

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeAnchored(
    velocityThreshold: () -> Float,
    initialAnchor: SwipeAnchor = SwipeAnchor.Partial,
    endOffset: (size: IntSize) -> Float = { it.height.toFloat() },
    startOffset: (size: IntSize) -> Float = { 0f },
    partialOffset: (size: IntSize) -> Float = { it.height * FIFTY_PERCENT },
    positionalThreshold: (Float) -> Float = { it * FIFTY_PERCENT },
    animationSpec: () -> AnimationSpec<Float> = { standardAccelerate() },
    allowNestedScroll: Boolean = true,
    onAnchorChange: suspend SwipeAnchoredScope.(SwipeAnchor) -> Unit = {},
    onOffset: (isInBetween: Boolean) -> Unit = { },
    content: @Composable SwipeAnchoredScope.() -> Unit
) {
    val anchoredDraggableState = rememberSaveable(
        saver = AnchoredDraggableState.Saver(
            animationSpec = animationSpec(),
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold
        )
    ) {
        AnchoredDraggableState(
            initialValue = initialAnchor,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
            animationSpec = animationSpec()
        )
    }
    val scope = remember {
        SwipeAnchoredScopeInstance(anchoredDraggableState)
    }

    LaunchedEffect(anchoredDraggableState.currentValue) {
        scope.onAnchorChange(anchoredDraggableState.currentValue)
    }

    val nestedScroll = remember {
        if (allowNestedScroll) {
            Modifier.nestedScroll(
                anchoredDraggableState.SwipeAnchoredNestedScrollConnection
            )
        } else {
            Modifier
        }
    }

    Box(
        modifier = Modifier
            .then(nestedScroll)
            .offset {
                val yOffset = anchoredDraggableState
                    .requireOffset()
                    .roundToInt()

                onOffset(anchoredDraggableState.isInBetween(yOffset))

                IntOffset(
                    x = 0,
                    y = yOffset
                )
            }
            .anchoredDraggable(
                state = anchoredDraggableState,
                orientation = Orientation.Vertical
            )
            .updateAnchors(
                anchoredDraggableState = anchoredDraggableState,
                startOffset = startOffset,
                partialOffset = partialOffset,
                endOffset = endOffset
            )
    ) {
        content(scope)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.updateAnchors(
    anchoredDraggableState: AnchoredDraggableState<SwipeAnchor>,
    startOffset: (size: IntSize) -> Float,
    partialOffset: (size: IntSize) -> Float,
    endOffset: (size: IntSize) -> Float
) = onSizeChanged { size ->
    val newAnchors = DraggableAnchors {
        SwipeAnchor.Start at startOffset(size)
        SwipeAnchor.Partial at partialOffset(size)
        SwipeAnchor.End at endOffset(size)
    }
    if (newAnchors != anchoredDraggableState.anchors) {
        anchoredDraggableState.updateAnchors(newAnchors)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun AnchoredDraggableState<SwipeAnchor>.isInBetween(yOffset: Int) =
    yOffset > anchors.minAnchor() && yOffset < anchors.maxAnchor()

@Preview(showBackground = true)
@Composable
private fun SwipeAnchoredPreview() {
    Box(
        modifier = Modifier
            .background(Color.Red)
    ) {
        val density = LocalDensity.current
        SwipeAnchored(
            velocityThreshold = { with(density) { 125.dp.toPx() } },
            positionalThreshold = { with(density) { 56.dp.toPx() } },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                )
            }
        )
    }
}
