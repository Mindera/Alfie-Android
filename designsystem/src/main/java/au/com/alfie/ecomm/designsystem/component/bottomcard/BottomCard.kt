package au.com.alfie.ecomm.designsystem.component.bottomcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.component.image.ratio.Ratio
import au.com.alfie.ecomm.designsystem.component.image.ratio.aspectRatio
import au.com.alfie.ecomm.designsystem.component.swipe.SwipeAnchor
import au.com.alfie.ecomm.designsystem.component.swipe.SwipeAnchored
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.coroutines.launch

private const val VELOCITY_THRESHOLD = 125
private const val POSITION_THRESHOLD = 56
private const val DEFAULT_PEEK_FRACTION = .66f

@Composable
fun BottomCard(
    backLayer: @Composable BottomCardScope.() -> Unit,
    frontLayer: @Composable BottomCardScope.() -> Unit,
    modifier: Modifier = Modifier,
    peekHeight: Dp = Dp.Unspecified,
    bottomStickyLayer: @Composable BottomCardScope.() -> Unit = {}
) {
    val scope = remember { BottomCardScopeInstance() }
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val peekHeightPx = with(density) { peekHeight.toPx() }
    var backLayerHeight by rememberSaveable { mutableFloatStateOf(0f) }
    var topCorner by remember { mutableStateOf(Theme.spacing.spacing0) }

    Column {
        Box(
            modifier = modifier then Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .onSizeChanged {
                        val newHeight = it.height.toFloat()
                        if (backLayerHeight != newHeight) {
                            backLayerHeight = newHeight
                            coroutineScope.launch {
                                scope.reset()
                            }
                        }
                    }
            ) {
                backLayer(scope)
            }
            SwipeAnchored(
                velocityThreshold = { with(density) { VELOCITY_THRESHOLD.dp.toPx() } },
                initialAnchor = SwipeAnchor.Start,
                startOffset = { backLayerHeight },
                partialOffset = {
                    if (peekHeightPx.isNaN()) {
                        backLayerHeight * DEFAULT_PEEK_FRACTION
                    } else {
                        peekHeightPx
                    }
                },
                endOffset = { 0f },
                positionalThreshold = { with(density) { POSITION_THRESHOLD.dp.toPx() } },
                onOffset = { isInBetween ->
                    topCorner = if (isInBetween) {
                        Theme.spacing.spacing12
                    } else {
                        Theme.spacing.spacing0
                    }
                }
            ) {
                scope.swipeAnchoredScope = this
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(topStart = topCorner, topEnd = topCorner)
                ) {
                    frontLayer(scope)
                }
            }
        }
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            bottomStickyLayer(scope)
        }
    }
}

@Preview
@Composable
private fun BottomCardPreview() {
    val coroutineScope = rememberCoroutineScope()

    BottomCard(
        peekHeight = 200.dp,
        backLayer = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(Ratio.RATIO3x4.ratioWidth, Ratio.RATIO3x4.ratioHeight)
                    .background(Color.Red)
            ) {
                Button(
                    type = ButtonType.Primary,
                    text = "Peek",
                    onClick = {
                        coroutineScope.launch {
                            peek()
                        }
                    }
                )
            }
        },
        frontLayer = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
            ) {
                repeat(100) {
                    item {
                        Text(text = "$it")
                    }
                }
            }
        }
    )
}
