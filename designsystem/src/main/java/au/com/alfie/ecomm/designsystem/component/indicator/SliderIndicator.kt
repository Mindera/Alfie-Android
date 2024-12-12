package au.com.alfie.ecomm.designsystem.component.indicator

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.commons.extension.orZero
import au.com.alfie.ecomm.designsystem.theme.Theme

private val SLIDER_WIDTH = 90.dp
private val SLIDER_HEIGHT = 4.dp
private const val DEFAULT_SLIDER_MIN_PERCENTAGE = 0.2F
private const val DEFAULT_SLIDER_MAX_PERCENTAGE = 0.8F

@Composable
fun SliderIndicator(
    state: LazyListState,
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.toDouble(), to = 1.toDouble(), fromInclusive = false, toInclusive = false)
    minPercentage: Float = DEFAULT_SLIDER_MIN_PERCENTAGE,
    @FloatRange(from = 0.toDouble(), to = 1.toDouble(), fromInclusive = false, toInclusive = false)
    maxPercentage: Float = DEFAULT_SLIDER_MAX_PERCENTAGE
) {
    val totalItemCount = remember { derivedStateOf { state.layoutInfo.totalItemsCount } }
    val viewPortSize = remember { derivedStateOf { state.layoutInfo.viewportSize } }
    val beforeContentPadding = remember { derivedStateOf { state.layoutInfo.beforeContentPadding } }
    val afterContentPadding = remember { derivedStateOf { state.layoutInfo.afterContentPadding } }
    val itemSpacing = remember { derivedStateOf { state.layoutInfo.mainAxisItemSpacing } }
    val visibleItems = remember { derivedStateOf { state.layoutInfo.visibleItemsInfo } }

    val itemLength = visibleItems.value.firstOrNull()?.size.orZero()
    val totalLength = itemLength * totalItemCount.value
    val totalSpacing = itemSpacing.value * (totalItemCount.value - 1) + beforeContentPadding.value + afterContentPadding.value
    val maxScroll = totalLength + totalSpacing - viewPortSize.value.width

    if (itemLength <= 0 || maxScroll <= 0) return

    val firstVisibleItemIndex = remember { derivedStateOf { state.firstVisibleItemIndex } }
    val scrolledItemLength = firstVisibleItemIndex.value * itemLength
    val scrolledItemSpacing = itemSpacing.value * firstVisibleItemIndex.value
    val visibleItemStartOffset = scrolledItemLength + scrolledItemSpacing
    val visibleItemEndOffset = remember { derivedStateOf { state.firstVisibleItemScrollOffset } }
    val scrolled = visibleItemStartOffset + visibleItemEndOffset.value

    SliderCanvas(
        minPercentage = minPercentage,
        maxPercentage = maxPercentage,
        scrollPosition = scrolled,
        maxScroll = maxScroll,
        modifier = modifier
    )
}

@Composable
fun SliderIndicator(
    state: ScrollState,
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.toDouble(), to = 1.toDouble(), fromInclusive = false, toInclusive = false)
    minPercentage: Float = DEFAULT_SLIDER_MIN_PERCENTAGE,
    @FloatRange(from = 0.toDouble(), to = 1.toDouble(), fromInclusive = false, toInclusive = false)
    maxPercentage: Float = DEFAULT_SLIDER_MAX_PERCENTAGE
) {
    SliderCanvas(
        minPercentage = minPercentage,
        maxPercentage = maxPercentage,
        scrollPosition = state.value,
        maxScroll = state.maxValue,
        modifier = modifier
    )
}

@Composable
fun SliderIndicator(
    currentItem: Int,
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .width(SLIDER_WIDTH)
            .height(SLIDER_HEIGHT)
    ) {
        val width = drawContext.size.width
        val height = drawContext.size.height

        val thumbLength = width / itemCount
        val barStart = thumbLength * currentItem
        val barEnd = barStart + thumbLength

        // Draw background line
        drawSliderLine(
            thickness = height,
            color = Theme.color.primary.mono200,
            startOffSet = 0F,
            endOffset = width
        )

        // Draw thumb indicating scroll progress
        drawSliderLine(
            thickness = height,
            color = Theme.color.primary.mono700,
            startOffSet = barStart,
            endOffset = barEnd
        )
    }
}

@Composable
private fun SliderCanvas(
    minPercentage: Float,
    maxPercentage: Float,
    scrollPosition: Int,
    maxScroll: Int,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .width(SLIDER_WIDTH)
            .height(SLIDER_HEIGHT)
    ) {
        val width = drawContext.size.width
        val height = drawContext.size.height

        val viewportLength = width - maxScroll
        val viewportRatio = viewportLength / maxScroll.toFloat()
        val ratio = viewportRatio.coerceIn(minPercentage, maxPercentage)

        val thumbLength = ratio * width
        val maxScrollLength = width - thumbLength

        val xOffset: Float = (scrollPosition / maxScroll.toFloat()) * maxScrollLength
        val barStart = if (layoutDirection == LayoutDirection.Ltr) xOffset else maxScrollLength - xOffset
        val barEnd = barStart + thumbLength

        // Draw background line
        drawSliderLine(
            thickness = height,
            color = Theme.color.primary.mono200,
            startOffSet = 0F,
            endOffset = width
        )

        // Draw thumb indicating scroll progress
        drawSliderLine(
            thickness = height,
            color = Theme.color.primary.mono700,
            startOffSet = barStart,
            endOffset = barEnd
        )
    }
}

private fun DrawScope.drawSliderLine(
    thickness: Float,
    color: Color,
    startOffSet: Float,
    endOffset: Float
) = drawLine(
    brush = SolidColor(color),
    start = Offset(x = startOffSet, y = 0F),
    end = Offset(x = endOffset, y = 0F),
    cap = StrokeCap.Round,
    strokeWidth = thickness
)

@Preview(showBackground = true)
@Composable
private fun LazySliderIndicatorPreview() {
    Theme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val state = rememberLazyListState()
            LazyRow(state = state) {
                items(count = 10) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .border(width = 3.dp, color = Theme.color.primary.mono900)
                    )
                }
            }
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            SliderIndicator(state = state)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScrollSliderIndicatorPreview() {
    Theme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val state = rememberScrollState()
            Row(modifier = Modifier.horizontalScroll(state)) {
                repeat(times = 10) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .border(width = 3.dp, color = Theme.color.primary.mono900)
                    )
                }
            }
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            SliderIndicator(state = state)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun PagedSliderIndicatorPreview() {
    Theme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val itemCount = 3
            val state = rememberPagerState { itemCount }
            HorizontalPager(state = state) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .border(width = 3.dp, color = Theme.color.primary.mono900)
                )
            }
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            SliderIndicator(
                currentItem = state.currentPage,
                itemCount = state.pageCount
            )
        }
    }
}
