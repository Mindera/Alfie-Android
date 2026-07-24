package com.mindera.alfie.designsystem.component.highlights

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.commons.string.toString
import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.designsystem.component.image.Image
import com.mindera.alfie.designsystem.component.image.ratio.Ratio
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import kotlinx.collections.immutable.persistentListOf

// D1: Pagination dot metrics have no design token (scale skips 6dp); values read directly off Figma
// node 4421:132068 and flagged as approximated in the handoff.
private val INACTIVE_DOT_SIZE = 6.dp
private val ACTIVE_DOT_WIDTH = 12.dp
private val DOT_HEIGHT = 6.dp

// D2: Figma scrim is a `multiply` linear gradient rgba(17,17,17,0.5)->transparent over the lower ~half of
// the card. Compose Box background has no multiply blend; a plain alpha scrim is visually equivalent for a
// darkening overlay. Stops approximated (transparent top half -> 0.5α bottom). Flagged in handoff.
private const val SCRIM_ALPHA = 0.5f
private const val SCRIM_START_STOP = 0.5f

/**
 * Editorial hero carousel — Figma "Highlights" (Design System file, node 4421:132068).
 *
 * A swipeable stack of full-bleed [HighlightsItem] cards (ratio 3:4), each with a bottom gradient scrim,
 * an overlaid [display.medium][com.mindera.alfie.designsystem.tokens.Typography] title and an optional
 * underlined inverted link, plus a page indicator (active pill + inactive dots).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Highlights(
    items: List<HighlightsItem>,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return
    val pagerState = rememberPagerState(initialPage = 0) { items.size }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxWidth()
    ) { page ->
        HighlightSlide(
            item = items[page],
            pageCount = items.size,
            currentPage = pagerState.currentPage
        )
    }
}

@Composable
private fun HighlightSlide(
    item: HighlightsItem,
    pageCount: Int,
    currentPage: Int
) {
    val theme = LocalTheme.current
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            imageUI = item.image,
            ratio = Ratio.RATIO3x4,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
        // D2: darkening scrim anchored to the bottom of the card.
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            SCRIM_START_STOP to Color.Transparent,
                            1f to theme.primitive.colors.neutrals800.copy(alpha = SCRIM_ALPHA)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                // D3: overlay padding = Figma `screen-size/margin` (16).
                .padding(theme.spacing.spacing16),
            verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing24),
            horizontalAlignment = Alignment.Start
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing8)) {
                Text(
                    text = item.title.toString(context),
                    style = theme.typography.display.medium,
                    color = theme.color.content.contentInvertedPrimary
                )
                if (item.actionText != null && item.onActionClick != null) {
                    Text(
                        text = item.actionText.toString(context),
                        style = theme.typography.link.medium.copy(textDecoration = TextDecoration.Underline),
                        color = theme.color.link.linkPrimaryInvertedDefault,
                        modifier = Modifier.clickable { item.onActionClick.invoke() }
                    )
                }
            }
            if (pageCount > 1) {
                HighlightsPagination(pageCount = pageCount, currentPage = currentPage)
            }
        }
    }
}

@Composable
private fun HighlightsPagination(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing8)
    ) {
        repeat(pageCount) { index ->
            val isSelected = currentPage == index
            // D1: active page = extended pill (button/primary background), inactive = disabled-grey dot.
            Box(
                modifier = Modifier
                    .width(if (isSelected) ACTIVE_DOT_WIDTH else INACTIVE_DOT_SIZE)
                    .height(DOT_HEIGHT)
                    .clip(theme.sizing.radius.rounded)
                    .background(
                        if (isSelected) {
                            theme.color.button.primaryBackgroundPrimaryDefault
                        } else {
                            theme.color.button.primaryBackgroundPrimaryDisabled
                        }
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HighlightsPreview() {
    Theme {
        Highlights(items = previewItems(count = 3))
    }
}

@Preview(showBackground = true)
@Composable
private fun HighlightsSingleSlidePreview() {
    Theme {
        Highlights(items = previewItems(count = 1))
    }
}

private fun previewItems(count: Int): List<HighlightsItem> = List(count) { index ->
    HighlightsItem(
        image = ImageUI(
            images = persistentListOf(
                ImageSizeUI.Large(
                    "https://images.pexels.com/photos/1926769/pexels-photo-1926769.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                )
            ),
            alt = ""
        ),
        title = StringResource.fromText("Transcending Trends\nfor Breezy Nights ${index + 1}"),
        actionText = StringResource.fromText("Explore Collection"),
        onActionClick = { }
    )
}
