package com.mindera.alfie.designsystem.component.highlights

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.commons.string.toString
import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.component.image.Image
import com.mindera.alfie.designsystem.component.image.ratio.Ratio
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

// D1: Figma's Pagination instance (node 4421:132068) is 40x6 and decomposes exactly to 12 + 6 + 6 with
// two 8dp gaps, so these are the design's own values, not approximations. They are literals only because
// the spacing scale skips 6dp — there is no token to point at.
private val INACTIVE_DOT_SIZE = 6.dp
private val ACTIVE_DOT_WIDTH = 12.dp
private val DOT_HEIGHT = 6.dp

// D2: Figma paints the scrim as a `multiply` layer of rgba(17,17,17,a). Compose backgrounds have no
// multiply blend, so the same colour is composited source-over. For a source this close to black the two
// agree exactly on a white backdrop and diverge by at most a * (17/255) ~= 8/255 on pure black, so the
// substitution is imperceptible rather than an approximation of the design.
//
// Dev mode gives `linear-gradient(to top, rgba(17,17,17,0.5) 19.25%, rgba(17,17,17,0) 52%)`. CSS `to top`
// measures from the bottom and verticalGradient from the top, so the stops invert: 52% -> 0.48 (fade
// begins), 19.25% -> 0.8075 (full scrim). Skia holds the terminal stop's colour past the last position,
// which is what produces the flat 0.5-alpha band across the bottom 19.25% that Figma specifies.
private const val SCRIM_ALPHA = 0.5f
private const val SCRIM_FADE_START_STOP = 0.48f
private const val SCRIM_FADE_END_STOP = 0.8075f

/**
 * Editorial hero carousel — Figma "Highlights" (Design System file, node 4421:132068).
 *
 * A swipeable stack of full-bleed [HighlightsItem] cards (ratio 3:4), each with a bottom gradient scrim,
 * an overlaid [display.medium][com.mindera.alfie.designsystem.tokens.Typography] title and an optional
 * underlined inverted link, plus a page indicator (active pill + inactive dots).
 */
@Composable
fun Highlights(
    items: ImmutableList<HighlightsItem>,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return
    val theme = LocalTheme.current
    val pagerState = rememberPagerState(initialPage = 0) { items.size }
    val hasPagination = items.size > 1

    // The indicator is hoisted out of the pager so it renders once and stays put: as page content it
    // translated with the swipe, showed two indicators mid-gesture, and re-announced its page count to
    // TalkBack for every composed page. Same shape as `NonZoomablePager` and `DotsIndicatorScreen`.
    Box(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1
        ) { page ->
            HighlightSlide(
                item = items[page],
                hasPagination = hasPagination
            )
        }
        if (hasPagination) {
            HighlightsPagination(
                pageCount = items.size,
                currentPage = { pagerState.currentPage },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(theme.spacing.spacing16)
            )
        }
    }
}

@Composable
private fun HighlightSlide(
    item: HighlightsItem,
    hasPagination: Boolean
) {
    val theme = LocalTheme.current
    val context = LocalContext.current
    // D3: overlay inset = Figma `screen-size/margin` (16). With the indicator shown the text block is
    // lifted clear of it: 16 (indicator inset) + 6 (indicator height) + 24 (Figma gap) = Figma's 500-454.
    val contentBottomPadding = if (hasPagination) {
        theme.spacing.spacing16 + DOT_HEIGHT + theme.spacing.spacing24
    } else {
        theme.spacing.spacing16
    }

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
                            SCRIM_FADE_START_STOP to Color.Transparent,
                            SCRIM_FADE_END_STOP to
                                theme.primitive.colors.neutrals800.copy(alpha = SCRIM_ALPHA)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(
                    start = theme.spacing.spacing16,
                    end = theme.spacing.spacing16,
                    bottom = contentBottomPadding
                ),
            verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing8)
        ) {
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
    }
}

@Composable
private fun HighlightsPagination(
    pageCount: Int,
    currentPage: () -> Int,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    // Deferred like `DotsIndicator(currentItem = ...)`, so settling a page recomposes the indicator
    // rather than the caller — `Highlights` never subscribes to `pagerState.currentPage`.
    val page = currentPage()
    // Expose page position to assistive tech — the dots are otherwise purely visual.
    val pageDescription = stringResource(
        R.string.highlights_pagination_content_description,
        page + 1,
        pageCount
    )
    Row(
        modifier = modifier.semantics { contentDescription = pageDescription },
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing8)
    ) {
        repeat(pageCount) { index ->
            val isSelected = page == index
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

private fun previewItems(count: Int): ImmutableList<HighlightsItem> = List(count) { index ->
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
}.toImmutableList()
