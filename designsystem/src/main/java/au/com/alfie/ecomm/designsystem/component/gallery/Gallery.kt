package au.com.alfie.ecomm.designsystem.component.gallery

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.media.GalleryUI
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.designsystem.animation.standard
import au.com.alfie.ecomm.designsystem.component.fullscreen.Fullscreen
import au.com.alfie.ecomm.designsystem.component.image.Image
import au.com.alfie.ecomm.designsystem.component.image.ratio.DimensionConstraint
import au.com.alfie.ecomm.designsystem.component.image.ratio.DimensionConstraint.MinParentDimension
import au.com.alfie.ecomm.designsystem.component.image.ratio.Ratio
import au.com.alfie.ecomm.designsystem.component.image.ratio.Ratio.RATIO3x4
import au.com.alfie.ecomm.designsystem.component.image.ratio.aspectRatio
import au.com.alfie.ecomm.designsystem.component.shimmer.shimmer
import au.com.alfie.ecomm.designsystem.component.swipe.SwipeAnchor
import au.com.alfie.ecomm.designsystem.component.swipe.SwipeAnchored
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.roundToInt

@Composable
fun Gallery(
    gallery: GalleryUI,
    ratio: Ratio,
    constraint: DimensionConstraint,
    modifier: Modifier = Modifier,
    startPosition: Int = 0,
    isLoading: Boolean = false,
    isFullscreen: Boolean = false,
    fullscreenRatio: Ratio = RATIO3x4,
    fullscreenConstraint: DimensionConstraint = MinParentDimension,
    onClick: ClickEventOneArg<Int> = {},
    onFavoriteClick: ClickEvent = {},
    onPositionChange: ClickEventOneArg<Int> = {},
    onDismissFullscreen: ClickEvent = {}
) {
    var selectedIndex by remember { mutableIntStateOf(startPosition) }
    var fullscreenSelectedIndex by remember { mutableIntStateOf(selectedIndex) }

    var galleryOffset by remember { mutableStateOf(IntOffset.Zero) }
    var resetOffset by remember { mutableStateOf(false) }

    val dismissFullscreen: (Boolean) -> Unit = { shouldResetOffset ->
        resetOffset = shouldResetOffset
        selectedIndex = fullscreenSelectedIndex
        onDismissFullscreen()
    }

    Box(
        modifier = modifier.shimmer(isShimmering = isLoading) then Modifier
            .run {
                if (isLoading) {
                    aspectRatio(
                        ratioWidth = ratio.ratioWidth,
                        ratioHeight = ratio.ratioHeight,
                        reference = constraint
                    )
                } else {
                    this
                }
            }
    ) {
        if (gallery.isEmpty()) return

        EndlessGallery(
            gallery = gallery,
            startPosition = selectedIndex,
            isZoomable = false,
            onPositionChange = onPositionChange,
            onFavoriteClick = onFavoriteClick
        ) {
            Box(
                modifier = Modifier
                    .clickable(
                        indication = ripple(bounded = false),
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            selectedIndex = index
                            onClick(index)
                        }
                    )
                    .onGloballyPositioned {
                        galleryOffset = it
                            .positionInRoot()
                            .toIntOffset()
                    }
            ) {
                Media(ratio, constraint)
            }
        }
    }

    Fullscreen(
        isOpen = isFullscreen,
        onDismissFullscreen = { dismissFullscreen(true) }
    ) {
        var fullscreenGalleryOffset by remember { mutableStateOf(galleryOffset) }

        val offsetState by animateIntOffsetAsState(
            targetValue = if (isFullscreen.not() && resetOffset) galleryOffset else fullscreenGalleryOffset,
            animationSpec = standard(),
            label = "fullscreen offset"
        )

        EndlessGallery(
            gallery = gallery,
            startPosition = selectedIndex,
            isZoomable = true,
            onPositionChange = {
                fullscreenSelectedIndex = it
                onPositionChange(it)
            }
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .offset { offsetState }
                        .onGloballyPositioned {
                            val verticalCenter = constraints.maxHeight / 2
                            val middle = it.size.height / 2
                            val yOffset = verticalCenter - middle

                            fullscreenGalleryOffset = IntOffset(x = 0, y = yOffset)
                        }
                ) {
                    val velocityThreshold = with(LocalDensity.current) { 125.dp.toPx() }
                    SwipeAnchored(
                        velocityThreshold = { velocityThreshold },
                        endOffset = { size -> size.height * .2f },
                        startOffset = { _ -> galleryOffset.y.toFloat() - fullscreenGalleryOffset.y.toFloat() },
                        partialOffset = { _ -> 0f },
                        allowNestedScroll = false,
                        onAnchorChange = { anchor ->
                            when (anchor) {
                                SwipeAnchor.Start -> dismissFullscreen(false)
                                SwipeAnchor.End -> animateTo(SwipeAnchor.Start)
                                else -> Unit
                            }
                        }
                    ) {
                        Media(fullscreenRatio, fullscreenConstraint)
                    }
                }
            }
        }
    }
}

@Composable
private fun EndlessGalleryScope.Media(
    ratio: Ratio,
    constraint: DimensionConstraint
) {
    when (mediaUI) {
        is ImageUI -> Image(
            imageUI = mediaUI,
            ratio = ratio,
            constraint = constraint
        )
        // TODO: add videos
    }
}

private fun Offset.toIntOffset() = IntOffset(x = 0, y = y.roundToInt())

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun GalleryPreview() {
    val galleryUI = GalleryUI(
        persistentListOf(
            ImageUI(
                images = persistentListOf(ImageSizeUI.Large(url = "https://images.pexels.com/photos/1304647/pexels-photo-1304647.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
                alt = ""
            ),
            ImageUI(
                images = persistentListOf(ImageSizeUI.Large(url = "https://images.pexels.com/photos/1820656/pexels-photo-1820656.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
                alt = ""
            ),
            ImageUI(
                images = persistentListOf(ImageSizeUI.Large(url = "https://images.pexels.com/photos/4112053/pexels-photo-4112053.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
                alt = ""
            )
        )
    )

    Gallery(
        gallery = galleryUI,
        ratio = RATIO3x4,
        constraint = DimensionConstraint.ParentWidth,
        onClick = {},
        onPositionChange = {}
    )
}
