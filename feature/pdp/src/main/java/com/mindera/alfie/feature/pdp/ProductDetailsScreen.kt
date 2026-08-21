package com.mindera.alfie.feature.pdp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.commons.string.toString
import com.mindera.alfie.core.commons.util.IntentUtils
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.navigation.arguments.ProductDetailsNavArgs
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.core.ui.media.GalleryUI
import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.core.ui.util.stringResource
import com.mindera.alfie.designsystem.component.accordion.Accordion
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonSize
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.component.gallery.Gallery
import com.mindera.alfie.designsystem.component.image.ratio.DimensionConstraint.ParentWidth
import com.mindera.alfie.designsystem.component.image.ratio.Ratio.RATIO3x4
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.shimmer.shimmer
import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonProperties
import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonState
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomHostState
import com.mindera.alfie.designsystem.component.swatch.SwatchType
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.action.TopBarAction
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.model.ApiErrorType
import com.mindera.alfie.feature.model.toStringRes
import com.mindera.alfie.feature.pdp.component.ProductDetailsColourCards
import com.mindera.alfie.feature.pdp.component.ProductDetailsInfo
import com.mindera.alfie.feature.pdp.component.ProductDetailsSize
import com.mindera.alfie.feature.pdp.model.ColorUI
import com.mindera.alfie.feature.pdp.model.ProductDetailsEvent
import com.mindera.alfie.feature.pdp.model.ProductDetailsSectionItem
import com.mindera.alfie.feature.pdp.model.ProductDetailsShareInfo
import com.mindera.alfie.feature.pdp.model.ProductDetailsUI
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState
import com.mindera.alfie.feature.pdp.model.ShareEvent
import com.mindera.alfie.feature.pdp.model.SizeSectionUI
import com.mindera.alfie.feature.pdp.model.SizeUI
import com.mindera.alfie.feature.uievent.handleUIEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.mindera.alfie.feature.R as FeatureR

@Destination(navArgsDelegate = ProductDetailsNavArgs::class)
@Composable
internal fun ProductDetailsScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel: ProductDetailsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val actions = remember {
        persistentListOf(TopBarAction.Share { viewModel.handleEvent(ProductDetailsEvent.OnShareClick) })
    }
    val topBarTitle = remember(state) {
        (state as? ProductDetailsUIState.Data.Loaded)?.details?.name.orEmpty()
    }
    topBarState.textTopBar(
        title = topBarTitle,
        showNavigationIcon = true,
        isLeftAligned = false,
        actions = actions
    )

    bottomBarState.hideBottomBar()

    viewModel.handleUIEvents(
        navigator = navigator,
        navController = navController,
        directionProvider = directionProvider,
        snackbarHostState = snackbarHostState,
        onCustomEvent = { event ->
            if (event is ShareEvent) {
                IntentUtils.share(
                    context = context,
                    text = event.content.toString(context),
                    title = event.title
                )
            }
        }
    )

    when (state) {
        is ProductDetailsUIState.Data -> ProductDetailsScreenContent(
            state = state as ProductDetailsUIState.Data,
            onEvent = viewModel::handleEvent
        )
        is ProductDetailsUIState.Error -> ProductDetailsScreenError(
            errorType = (state as ProductDetailsUIState.Error).errorType,
            onRetry = viewModel::retry
        )
    }
}

/**
 * One continuous scroll per the modern design: full-bleed gallery, then the content block
 * (16px screen margins, 24px between sections) — product info with the CTA row beneath it,
 * the inline colour cards, the size selector, the description and the accordion rows.
 * The former bottom-sheet layering and the half-filled tablet two-column are gone; the same
 * scroll serves every window size (matching the iOS rollout).
 */
@Composable
private fun ProductDetailsScreenContent(
    state: ProductDetailsUIState.Data,
    onEvent: ClickEventOneArg<ProductDetailsEvent>
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var colourSectionOffset by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ) {
        ProductDetailsGallery(state = state)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Theme.spacing.spacing16,
                    vertical = Theme.spacing.spacing16
                ),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing24)
        ) {
            // Product Main Info holds its own 8px gap to the CTA row (screens file, "Frame 109").
            Column(verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing8)) {
                ProductDetailsInfo(
                    state = state,
                    onColourSummaryClick = {
                        scope.launch { scrollState.animateScrollTo(colourSectionOffset) }
                    }
                )
                ProductDetailsCtaRow(
                    state = state,
                    onEvent = onEvent
                )
            }
            if (state.details.colors.size > 1) {
                Box(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        colourSectionOffset = coordinates.positionInParent().y.roundToInt()
                    }
                ) {
                    ProductDetailsColourCards(
                        state = state,
                        onColorClick = onEvent
                    )
                }
            }
            ProductDetailsSize(
                state = state,
                onEvent = onEvent
            )
            ProductDetailsDescription(state = state)
            ProductDetailsSections(
                state = state,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun ProductDetailsScreenError(
    errorType: ApiErrorType,
    onRetry: () -> Unit
) {
    val c = LocalTheme.current.primitive.colors
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = AlfieIcons.AlertFill),
            contentDescription = null,
            tint = c.neutrals900,
            modifier = Modifier.size(Theme.iconSize.large)
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            text = stringResource(errorType.toStringRes(notFoundRes = R.string.product_details_product_not_found)),
            style = LocalTheme.current.typography.body.mediumBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Button(
            type = ButtonType.Secondary,
            buttonSize = ButtonSize.Medium,
            text = stringResource(FeatureR.string.retry),
            onClick = onRetry
        )
    }
}

@Composable
private fun ProductDetailsGallery(
    state: ProductDetailsUIState.Data
) {
    val isLoading = state is ProductDetailsUIState.Data.Loading
    var isFullscreen by remember { mutableStateOf(false) }

    Gallery(
        gallery = state.details.gallery,
        isWishlisted = state.details.isWishlisted,
        showWishlistButton = false,
        ratio = RATIO3x4,
        constraint = ParentWidth,
        isLoading = isLoading,
        isFullscreen = isFullscreen,
        onClick = { isFullscreen = true },
        onDismissFullscreen = { isFullscreen = false }
    )
}

// Frame 109: the primary Add to Bag fills the row next to a 40dp square outlined wishlist
// button — the wishlist moved off the gallery into this row and scrolls with the page.
@Composable
private fun ProductDetailsCtaRow(
    state: ProductDetailsUIState.Data,
    onEvent: ClickEventOneArg<ProductDetailsEvent>
) {
    val c = LocalTheme.current.primitive.colors
    val isLoading = state is ProductDetailsUIState.Data.Loading
    val hasSelectedSize = when (val sizeSection = state.details.sizeSectionUI) {
        is SizeSectionUI.SizeSelector -> sizeSection.selectedSize != null
        is SizeSectionUI.SingleSize,
        is SizeSectionUI.SizeOnly,
        is SizeSectionUI.NoSize -> true
    }
    val isSelectionSoldOut = state.details.isSelectionSoldOut
    val text = if (isSelectionSoldOut) {
        stringResource(id = R.string.product_details_size_out_of_stock)
    } else {
        stringResource(id = R.string.product_details_add_to_bag)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.weight(1f),
            type = ButtonType.Primary,
            isShimmering = isLoading,
            buttonSize = ButtonSize.Medium,
            text = text,
            isEnabled = hasSelectedSize && isSelectionSoldOut.not(),
            onClick = { onEvent(ProductDetailsEvent.OnAddToBagClick) }
        )
        val wishlistContentDescription = stringResource(id = R.string.product_details_wishlist_content_description)
        Box(
            modifier = Modifier
                .size(Theme.iconSize.xLarge)
                .border(
                    width = LocalTheme.current.primitive.border.weightDefault,
                    color = c.neutrals900,
                    shape = Theme.shape.none
                )
                .clickable { onEvent(ProductDetailsEvent.OnFavoriteClick(state.details.slug)) }
                .semantics { contentDescription = wishlistContentDescription },
            contentAlignment = Alignment.Center
        ) {
            val iconRes = if (state.details.isWishlisted) AlfieIcons.WishlistFill else AlfieIcons.Wishlist
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = c.neutrals800,
                modifier = Modifier.size(Theme.iconSize.medium)
            )
        }
    }
}

// Description (screens file): the lead paragraph in body/medium, then the
// "Colour Name | Ref. 0273/393" metadata line in label/small content-terciary — each half
// omitted rather than rendered empty when the data is missing.
@Composable
private fun ProductDetailsDescription(state: ProductDetailsUIState.Data) {
    val c = LocalTheme.current.primitive.colors
    val isLoading = state is ProductDetailsUIState.Data.Loading
    val details = state.details
    val colourName = details.selectedColourName
    val reference = details.productReference

    Column(verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16)) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(
                    isShimmering = isLoading,
                    xScale = Theme.scale.scale80
                ),
            text = details.description,
            style = LocalTheme.current.typography.body.medium,
            color = c.neutrals800
        )
        if (!isLoading && (colourName != null || reference != null)) {
            val style = LocalTheme.current.typography.label.small
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .let { modifier ->
                        if (colourName != null && reference != null) {
                            val a11yLabel = stringResource(
                                id = R.string.product_details_colour_and_reference_a11y,
                                colourName,
                                reference
                            )
                            modifier.semantics { contentDescription = a11yLabel }
                        } else {
                            modifier
                        }
                    },
                horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing4)
            ) {
                colourName?.let {
                    Text(text = it, style = style, color = c.neutrals500)
                }
                if (colourName != null && reference != null) {
                    Text(text = "|", style = style, color = c.neutrals500)
                }
                reference?.let {
                    Text(
                        text = stringResource(id = R.string.product_details_reference, it),
                        style = style,
                        color = c.neutrals500
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDetailsSections(
    state: ProductDetailsUIState.Data,
    onEvent: ClickEventOneArg<ProductDetailsEvent>
) {
    val c = LocalTheme.current.primitive.colors
    val isLoading = state is ProductDetailsUIState.Data.Loading
    val sections = state.details.sections

    Column(modifier = Modifier.fillMaxWidth()) {
        sections.forEach { section ->
            val title = stringResource(resource = section.title)
            Accordion(
                title = if (isLoading) "" else title,
                content = {
                    Text(
                        text = stringResource(id = R.string.product_details_section_view_link, title),
                        style = LocalTheme.current.typography.body.mediumBold,
                        color = c.neutrals800,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clipToBounds()
                            .clickable { onEvent(ProductDetailsEvent.OnSectionClick(section)) }
                            .padding(vertical = Theme.spacing.spacing8)
                            .shimmer(isShimmering = isLoading)
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PdpScreenPreview() {
    Theme {
        ProductDetailsScreenContent(
            state = ProductDetailsUIState.Data.Loaded(
                details = ProductDetailsUI(
                    id = "123456",
                    brand = "Givenchy",
                    name = "Seamless sculpt mid thigh short",
                    slug = "slug",
                    description = "Blazer made from 100% cotton fabric. Collar with notched lapel.",
                    price = PriceType.Sale(fullPrice = "£10", salePrice = "£8"),
                    variants = persistentListOf(),
                    isSelectionSoldOut = false,
                    colors = persistentListOf(
                        ColorUI(
                            id = "Black",
                            type = SwatchType.PlainColor(Color.Black, true),
                            index = 0
                        ),
                        ColorUI(
                            id = "Blue",
                            type = SwatchType.PlainColor(Color.Blue, true),
                            index = 1
                        )
                    ),
                    sections = persistentListOf(
                        ProductDetailsSectionItem(
                            title = StringResource.fromId(R.string.product_details_section_delivery_and_returns),
                            url = ""
                        )
                    ),
                    shareInfo = ProductDetailsShareInfo.EMPTY,
                    gallery = GalleryUI(
                        medias = persistentListOf(
                            ImageUI(
                                images = persistentListOf(
                                    ImageSizeUI.Large(
                                        url = "https://images.pexels.com/photos/1680172/pexels-photo-1680172.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                                    )
                                ),
                                alt = ""
                            ),
                            ImageUI(
                                images = persistentListOf(
                                    ImageSizeUI.Large(
                                        url = "https://images.pexels.com/photos/2896837/pexels-photo-2896837.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                                    )
                                ),
                                alt = ""
                            ),
                            ImageUI(
                                images = persistentListOf(
                                    ImageSizeUI.Large(
                                        url = "https://images.pexels.com/photos/6311251/pexels-photo-6311251.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                                    )
                                ),
                                alt = ""
                            )
                        )
                    ),
                    sizeSectionUI = SizeSectionUI.SizeSelector(
                        sizes = persistentListOf(
                            SizeUI(
                                id = "XS",
                                properties = SizingButtonProperties(text = "XS", state = SizingButtonState.Selectable)
                            ),
                            SizeUI(
                                id = "S",
                                properties = SizingButtonProperties(text = "S", state = SizingButtonState.Selectable)
                            ),
                            SizeUI(
                                id = "M",
                                properties = SizingButtonProperties(text = "M", state = SizingButtonState.OutOfStock)
                            )
                        )
                    )
                )
            ),
            onEvent = { }
        )
    }
}
