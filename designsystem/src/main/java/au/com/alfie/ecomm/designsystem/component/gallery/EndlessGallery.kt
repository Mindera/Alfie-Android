package au.com.alfie.ecomm.designsystem.component.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.media.GalleryUI
import au.com.alfie.ecomm.core.ui.media.MediaUI
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.ScrollGesturePropagation.ContentEdge
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

private const val PAGES_COUNT = 1500
private const val PAGES_COUNT_MINIMUM = 1

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EndlessGallery(
    gallery: GalleryUI,
    startPosition: Int,
    isZoomable: Boolean,
    onPositionChange: (Int) -> Unit,
    onFavoriteClick: ClickEvent = {},
    content: @Composable EndlessGalleryScope.() -> Unit
) {
    val itemsCount = gallery.size
    val numberPages = PAGES_COUNT / itemsCount
    val startIndex = (numberPages / 2) * itemsCount + startPosition
    val pagerState: PagerState = rememberPagerState(initialPage = startIndex) {
        if (itemsCount > 1) PAGES_COUNT else PAGES_COUNT_MINIMUM
    }

    LaunchedEffect(startPosition) {
        if (pagerState.currentPage % itemsCount != startPosition) {
            pagerState.scrollToPage(startIndex)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        onPositionChange(pagerState.currentPage % itemsCount)
    }

    if (isZoomable) {
        ZoomableEndlessGallery(
            pagerState = pagerState,
            itemsCount = itemsCount,
            gallery = gallery,
            content = content
        )
    } else {
        NonZoomableEndlessGallery(
            pagerState = pagerState,
            itemsCount = itemsCount,
            gallery = gallery,
            onFavoriteClick = onFavoriteClick,
            content = content
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ZoomableEndlessGallery(
    pagerState: PagerState,
    itemsCount: Int,
    gallery: GalleryUI,
    content: @Composable EndlessGalleryScope.() -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ZoomablePager(
            pagerState = pagerState,
            itemsCount = itemsCount,
            gallery = gallery,
            content = content
        )
        if (itemsCount > 1) {
            Indicator(
                pagerState = pagerState,
                itemsCount = itemsCount,
                modifier = Modifier.padding(bottom = Theme.spacing.spacing16)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NonZoomableEndlessGallery(
    pagerState: PagerState,
    itemsCount: Int,
    gallery: GalleryUI,
    onFavoriteClick: ClickEvent,
    content: @Composable EndlessGalleryScope.() -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        NonZoomablePager(
            pagerState = pagerState,
            itemsCount = itemsCount,
            gallery = gallery,
            onFavoriteClick = onFavoriteClick,
            content = content
        )
        if (itemsCount > 1) {
            Indicator(
                pagerState = pagerState,
                itemsCount = itemsCount
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ZoomablePager(
    pagerState: PagerState,
    itemsCount: Int,
    gallery: GalleryUI,
    content: @Composable EndlessGalleryScope.() -> Unit
) {
    HorizontalPager(state = pagerState) { index ->
        val itemIndex = index % itemsCount

        Box(
            modifier = Modifier.zoomable(
                zoomState = rememberZoomState(),
                scrollGesturePropagation = ContentEdge
            )
        ) {
            content(
                EndlessGalleryScope(
                    mediaUI = gallery[itemIndex],
                    index = itemIndex
                )
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun NonZoomablePager(
    pagerState: PagerState,
    itemsCount: Int,
    gallery: GalleryUI,
    onFavoriteClick: ClickEvent,
    content: @Composable EndlessGalleryScope.() -> Unit
) {
    val horizontalPadding = if (itemsCount > 1) Theme.spacing.spacing32 else Theme.spacing.spacing16

    Box(contentAlignment = Alignment.TopEnd) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            pageSpacing = Theme.spacing.spacing8,
            contentPadding = PaddingValues(horizontal = horizontalPadding)
        ) { index ->
            val itemIndex = index % itemsCount

            Box(modifier = Modifier.clip(Theme.shape.small)) {
                content(
                    EndlessGalleryScope(
                        mediaUI = gallery[itemIndex],
                        index = itemIndex
                    )
                )
            }
        }
        IconButton(
            modifier = Modifier
                .padding(
                    end = horizontalPadding,
                    top = Theme.spacing.spacing4
                )
                .size(Theme.iconSize.xLarge),
            onClick = onFavoriteClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_action_heart_outline),
                contentDescription = null,
                modifier = Modifier.size(Theme.iconSize.medium)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Indicator(
    pagerState: PagerState,
    itemsCount: Int,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    GalleryIndicator(
        currentItem = pagerState.currentPage % itemsCount + 1,
        itemCount = itemsCount,
        onLeftClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        },
        onRightClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        },
        modifier = modifier
    )
}

@Immutable
internal class EndlessGalleryScope(
    val mediaUI: MediaUI,
    val index: Int
)
