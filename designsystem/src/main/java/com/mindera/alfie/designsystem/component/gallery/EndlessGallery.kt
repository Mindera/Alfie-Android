package com.mindera.alfie.designsystem.component.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.media.GalleryUI
import com.mindera.alfie.core.ui.media.MediaUI
import com.mindera.alfie.designsystem.component.indicator.PageIndicator
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import net.engawapg.lib.zoomable.ScrollGesturePropagation.ContentEdge
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

private const val PAGES_COUNT = 1500
private const val PAGES_COUNT_MINIMUM = 1

// PDP gallery pagination sits 12px above the bottom edge of the imagery
private val GALLERY_INDICATOR_BOTTOM_PADDING = 12.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EndlessGallery(
    gallery: GalleryUI,
    isWishlisted: Boolean = false,
    showWishlistButton: Boolean = true,
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
            isWishlisted = isWishlisted,
            showWishlistButton = showWishlistButton,
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
            PageIndicator(
                currentItem = pagerState.currentPage % itemsCount,
                itemCount = itemsCount,
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
    isWishlisted: Boolean,
    showWishlistButton: Boolean,
    onFavoriteClick: ClickEvent,
    content: @Composable EndlessGalleryScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        NonZoomablePager(
            pagerState = pagerState,
            itemsCount = itemsCount,
            gallery = gallery,
            isWishlisted = isWishlisted,
            showWishlistButton = showWishlistButton,
            onFavoriteClick = onFavoriteClick,
            content = content
        )
        if (itemsCount > 1) {
            PageIndicator(
                currentItem = pagerState.currentPage % itemsCount,
                itemCount = itemsCount,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = GALLERY_INDICATOR_BOTTOM_PADDING)
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
    isWishlisted: Boolean,
    showWishlistButton: Boolean,
    onFavoriteClick: ClickEvent,
    content: @Composable EndlessGalleryScope.() -> Unit
) {
    Box(contentAlignment = Alignment.TopEnd) {
        HorizontalPager(state = pagerState) { index ->
            val itemIndex = index % itemsCount

            content(
                EndlessGalleryScope(
                    mediaUI = gallery[itemIndex],
                    index = itemIndex
                )
            )
        }
        if (showWishlistButton) {
            IconButton(
                modifier = Modifier
                    .padding(top = Theme.spacing.spacing4)
                    .size(Theme.iconSize.xLarge),
                onClick = onFavoriteClick
            ) {
                val iconRes =
                    if (isWishlisted) AlfieIcons.WishlistFill else AlfieIcons.Wishlist

                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(Theme.iconSize.medium)
                )
            }
        }
    }
}

@Immutable
internal class EndlessGalleryScope(
    val mediaUI: MediaUI,
    val index: Int
)
