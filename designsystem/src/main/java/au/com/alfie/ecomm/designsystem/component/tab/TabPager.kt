package au.com.alfie.ecomm.designsystem.component.tab

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.util.fastForEachIndexed
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.ui.util.stringResource
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.divider.DividerType
import au.com.alfie.ecomm.designsystem.component.divider.HorizontalDivider
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

private const val PAGE_COUNT_OFFSET = 2
private const val TAB_ANIMATION_DURATION = 250

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollableTabPager(
    items: ImmutableList<TabItem>,
    modifier: Modifier = Modifier,
    beyondBoundsPageCount: Int = PAGE_COUNT_OFFSET,
    isUserScrollEnabled: Boolean = true,
    isCompact: Boolean = true,
    isLight: Boolean = true,
    content: @Composable (Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { items.size })
    var tabSizes by remember { mutableStateOf(List(items.size) { Size.Zero }) }

    Column(modifier = modifier) {
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            containerColor = Theme.color.white,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabIndicator(
                    position = tabPositions[pagerState.currentPage],
                    tabSize = tabSizes[pagerState.currentPage],
                    isLight = isLight
                )
            },
            divider = {}
        ) {
            items.fastForEachIndexed { index, item ->
                TabItem(
                    index = index,
                    item = item,
                    pagerState = pagerState,
                    isCompact = isCompact,
                    onContentSizeChange = { size ->
                        tabSizes = tabSizes.mapIndexed { tabIndex, previousSize ->
                            if (tabIndex == index) size else previousSize
                        }
                    }
                )
            }
        }
        HorizontalDivider(dividerType = DividerType.Solid1Mono200)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PagerContent(
                pagerState = pagerState,
                beyondBoundsPageCount = beyondBoundsPageCount,
                isUserScrollEnabled = isUserScrollEnabled,
                content = content
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FixedTabPager(
    items: ImmutableList<TabItem>,
    modifier: Modifier = Modifier,
    beyondBoundsPageCount: Int = PAGE_COUNT_OFFSET,
    isUserScrollEnabled: Boolean = true,
    isCompact: Boolean = true,
    isLight: Boolean = true,
    content: @Composable (Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { items.size })
    var tabSizes by remember { mutableStateOf(List(items.size) { Size.Zero }) }

    Column(modifier = modifier) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            containerColor = Theme.color.white,
            indicator = { tabPositions ->
                TabIndicator(
                    position = tabPositions[pagerState.currentPage],
                    tabSize = tabSizes[pagerState.currentPage],
                    isLight = isLight
                )
            },
            divider = {
                HorizontalDivider(dividerType = DividerType.Solid1Mono200)
            }
        ) {
            items.fastForEachIndexed { index, item ->
                TabItem(
                    index = index,
                    item = item,
                    pagerState = pagerState,
                    isCompact = isCompact,
                    onContentSizeChange = { size ->
                        tabSizes = tabSizes.mapIndexed { tabIndex, previousSize ->
                            if (tabIndex == index) size else previousSize
                        }
                    }
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PagerContent(
                pagerState = pagerState,
                beyondBoundsPageCount = beyondBoundsPageCount,
                isUserScrollEnabled = isUserScrollEnabled,
                content = content
            )
        }
    }
}

@Composable
private fun TabIndicator(
    position: TabPosition,
    tabSize: Size,
    isLight: Boolean
) {
    val clipShape = Theme.shape.tiny.copy(
        bottomStart = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp)
    )
    val indicatorColor = if (isLight) Theme.color.primary.mono200 else Theme.color.primary.mono900

    return TabRowDefaults.SecondaryIndicator(
        modifier = Modifier
            .tabIndicatorOffset(
                currentTabPosition = position,
                tabContentSize = tabSize
            )
            .clip(clipShape),
        color = indicatorColor,
        height = 4.dp
    )
}

// Custom tabIndicatorOffset modifier that accepts the actual tab item content size
// Inspired by the Material's tabIndicatorOffset modifier
@Composable
fun Modifier.tabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabContentSize: Size
): Modifier {
    val tabContentWidth = with(LocalDensity.current) { tabContentSize.width.toDp() }
    val currentTabWidth by animateDpAsState(
        targetValue = tabContentWidth,
        animationSpec = tween(durationMillis = TAB_ANIMATION_DURATION, easing = FastOutSlowInEasing),
        label = "TabWidthAnimation"
    )
    val tabContentOffset = currentTabPosition.left + ((currentTabPosition.width - tabContentWidth) / 2)
    val indicatorOffset by animateDpAsState(
        targetValue = tabContentOffset,
        animationSpec = tween(durationMillis = TAB_ANIMATION_DURATION, easing = FastOutSlowInEasing),
        label = "TabLeftOffsetAnimation"
    )
    return this then fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset { IntOffset(x = indicatorOffset.roundToPx(), y = 0) }
        .width(currentTabWidth)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabItem(
    index: Int,
    item: TabItem,
    pagerState: PagerState,
    isCompact: Boolean,
    onContentSizeChange: (Size) -> Unit
) {
    val scope = rememberCoroutineScope()
    val isSelected = index == pagerState.currentPage

    Tab(
        selected = isSelected,
        onClick = {
            scope.launch {
                pagerState.animateScrollToPage(page = index)
            }
        }
    ) {
        val contentColor = if (isSelected) Theme.color.primary.mono900 else Theme.color.primary.mono500
        val horizontalPadding = if (isCompact) Theme.spacing.spacing4 else Theme.spacing.spacing8

        Column(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .onSizeChanged { size ->
                    onContentSizeChange(size.toSize())
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .heightIn(38.dp)
                    .padding(horizontal = horizontalPadding)
            ) {
                item.icon?.let { icon ->
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.size(Theme.iconSize.small),
                        tint = contentColor
                    )
                    Spacer(modifier = Modifier.width(Theme.spacing.spacing4))
                }
                Text(
                    text = stringResource(resource = item.label),
                    style = Theme.typography.paragraph,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(Theme.spacing.spacing4))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PagerContent(
    pagerState: PagerState,
    beyondBoundsPageCount: Int,
    isUserScrollEnabled: Boolean,
    content: @Composable (Int) -> Unit
) {
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        beyondViewportPageCount = beyondBoundsPageCount,
        userScrollEnabled = isUserScrollEnabled
    ) { page ->
        content(page)
    }
}

@Preview(showBackground = true)
@Composable
private fun ScrollableTabPagerPreview() {
    Theme {
        val items = persistentListOf(
            TabItem(StringResource.fromText("Shop All Women's Sale"), R.drawable.ic_action_gift),
            TabItem(StringResource.fromText("Shop All Men's Sale")),
            TabItem(StringResource.fromText("Shop All Kid's Sale"))
        )

        ScrollableTabPager(
            items = items,
            isCompact = true
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(resource = items[page].label))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FixedTabPagerPreview() {
    Theme {
        val items = persistentListOf(
            TabItem(StringResource.fromText("Description")),
            TabItem(StringResource.fromText("Reviews")),
            TabItem(StringResource.fromText("Promotion"))
        )

        FixedTabPager(
            items = items,
            isCompact = true
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(resource = items[page].label))
            }
        }
    }
}
