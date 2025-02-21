package au.com.alfie.ecomm.feature.pdp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.commons.string.toString
import au.com.alfie.ecomm.core.commons.util.IntentUtils
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.navigation.arguments.ProductDetailsNavArgs
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.extension.handleWindowType
import au.com.alfie.ecomm.core.ui.media.GalleryUI
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.core.ui.util.stringResource
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.bottomcard.BottomCard
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonSize
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.component.divider.DividerType
import au.com.alfie.ecomm.designsystem.component.divider.HorizontalDivider
import au.com.alfie.ecomm.designsystem.component.gallery.Gallery
import au.com.alfie.ecomm.designsystem.component.image.ratio.DimensionConstraint.ParentWidth
import au.com.alfie.ecomm.designsystem.component.image.ratio.Ratio.RATIO3x4
import au.com.alfie.ecomm.designsystem.component.listitem.ListItemWithShimmering
import au.com.alfie.ecomm.designsystem.component.shimmer.shimmer
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomHostState
import au.com.alfie.ecomm.designsystem.component.swatch.SwatchSize
import au.com.alfie.ecomm.designsystem.component.swatch.SwatchType
import au.com.alfie.ecomm.designsystem.component.tab.FixedTabPager
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.action.TopBarAction
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.pdp.component.ProductDetailsColorPicker
import au.com.alfie.ecomm.feature.pdp.component.ProductDetailsSize
import au.com.alfie.ecomm.feature.pdp.model.ColorUI
import au.com.alfie.ecomm.feature.pdp.model.InformationUI
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsEvent
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsSectionItem
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsShareInfo
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsUI
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsUIState
import au.com.alfie.ecomm.feature.pdp.model.ShareEvent
import au.com.alfie.ecomm.feature.pdp.model.SizeSectionUI
import au.com.alfie.ecomm.feature.uievent.handleUIEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import au.com.alfie.ecomm.designsystem.R as RD

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
        (state as? ProductDetailsUIState.Data.Loaded)?.details?.brand.orEmpty()
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
        is ProductDetailsUIState.Error -> ProductDetailsScreenError()
    }
}

@Composable
private fun ProductDetailsScreenContent(
    state: ProductDetailsUIState.Data,
    onEvent: ClickEventOneArg<ProductDetailsEvent>
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        handleWindowType(
            onCompactWindow = {
                MobileLayout(
                    state = state,
                    onEvent = onEvent
                )
            },
            onNotCompactWindow = {
                TabletLayout(
                    state = state,
                    onEvent = onEvent
                )
            }
        )
    }
}

@Composable
private fun MobileLayout(
    state: ProductDetailsUIState.Data,
    onEvent: ClickEventOneArg<ProductDetailsEvent>
) {
    BottomCard(
        backLayer = {
            Column {
                Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
                ProductDetailsGallery(
                    state = state,
                    onEvent = onEvent
                )
            }
        },
        frontLayer = {
            ProductDetailsLayer(
                state = state,
                onEvent = onEvent
            )
        },
        bottomStickyLayer = {
            ProductDetailsBagSection(
                state = state,
                onEvent = onEvent
            )
        }
    )
}

@Composable
private fun TabletLayout(
    state: ProductDetailsUIState.Data,
    onEvent: ClickEventOneArg<ProductDetailsEvent>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Theme.spacing.spacing32)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            ProductDetailsGallery(
                state = state,
                onEvent = onEvent
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing20))
            ProductDetailsName(state = state)
            Spacer(modifier = Modifier.height(Theme.spacing.spacing20))
            ProductDetailsColorPicker(
                swatchSize = SwatchSize.Small,
                state = state,
                onColorClick = onEvent
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // TODO Add missing content
        }
    }
}

@Composable
private fun ProductDetailsScreenError() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = RD.drawable.ic_informational_warning),
            contentDescription = null,
            tint = Theme.color.black,
            modifier = Modifier.size(Theme.iconSize.large)
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            text = stringResource(R.string.product_details_product_not_found),
            style = Theme.typography.paragraphBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ProductDetailsGallery(
    state: ProductDetailsUIState.Data,
    onEvent: ClickEventOneArg<ProductDetailsEvent>
) {
    val isLoading = state is ProductDetailsUIState.Data.Loading
    var isFullscreen by remember { mutableStateOf(false) }

    Gallery(
        gallery = state.details.gallery,
        ratio = RATIO3x4,
        constraint = ParentWidth,
        isLoading = isLoading,
        isFullscreen = isFullscreen,
        onClick = { isFullscreen = true },
        onDismissFullscreen = { isFullscreen = false },
        onFavoriteClick = { onEvent(ProductDetailsEvent.OnFavoriteClick) }
    )
}

@Composable
private fun ProductDetailsLayer(
    state: ProductDetailsUIState.Data,
    onEvent: ClickEventOneArg<ProductDetailsEvent>
) {
    Column(
        modifier = Modifier
            .padding(horizontal = Theme.spacing.spacing16)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        ProductDetailsName(state = state)
        Spacer(modifier = Modifier.height(Theme.spacing.spacing20))
        ProductDetailsColorPicker(
            swatchSize = SwatchSize.Small,
            state = state,
            onColorClick = onEvent
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing4))
        ProductDetailsSize(
            state = state,
            onEvent = onEvent
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing20))
        ProductDetailsInformation(state = state)
        Spacer(modifier = Modifier.height(Theme.spacing.spacing20))
        ProductDetailsSections(
            state = state,
            onEvent = onEvent
        )
    }
}

@Composable
private fun ProductDetailsName(state: ProductDetailsUIState.Data) {
    val isLoading = state is ProductDetailsUIState.Data.Loading

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(
                isShimmering = isLoading,
                xScale = Theme.scale.scale80
            ),
        text = state.details.name,
        style = Theme.typography.paragraphLarge,
        color = Theme.color.black
    )
}

@Composable
private fun ProductDetailsSections(
    state: ProductDetailsUIState.Data,
    onEvent: ClickEventOneArg<ProductDetailsEvent>
) {
    val isLoading = state is ProductDetailsUIState.Data.Loading
    val sections = state.details.sections

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(dividerType = DividerType.Solid1Mono200)

        sections.forEach { section ->
            ListItemWithShimmering(
                isLoading = isLoading,
                headlineContent = { modifier ->
                    Text(
                        modifier = modifier,
                        text = if (isLoading) "" else stringResource(resource = section.title),
                        style = Theme.typography.paragraph,
                        color = Theme.color.primary.mono700
                    )
                },
                trailingContent = {
                    Icon(
                        modifier = Modifier.size(Theme.iconSize.small),
                        painter = painterResource(id = RD.drawable.ic_action_chevron_right),
                        contentDescription = null,
                        tint = Theme.color.primary.mono700
                    )
                },
                modifier = Modifier
                    .padding(vertical = Theme.spacing.spacing8)
                    .clickable { onEvent(ProductDetailsEvent.OnSectionClick(section)) }
            )

            HorizontalDivider(dividerType = DividerType.Solid1Mono200)
        }
    }
}

@Composable
private fun ProductDetailsBagSection(
    state: ProductDetailsUIState.Data,
    onEvent: ClickEventOneArg<ProductDetailsEvent>
) {
    val isLoading = state is ProductDetailsUIState.Data.Loading
    val text = if (state.details.isSelectionSoldOut) {
        stringResource(id = R.string.product_details_size_out_of_stock)
    } else {
        stringResource(id = R.string.product_details_add_to_bag)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Theme.spacing.spacing8,
                    horizontal = Theme.spacing.spacing16
                ),
            type = ButtonType.Primary,
            isShimmering = isLoading,
            buttonSize = ButtonSize.Medium,
            text = text,
            isEnabled = state.details.isSelectionSoldOut.not(),
            onClick = {
                val event = ProductDetailsEvent.OnAddToBagClick(state.details)
                onEvent(event)
            }
        )
    }
}

@Composable
private fun ProductDetailsInformation(state: ProductDetailsUIState.Data) {
    val isLoading = state is ProductDetailsUIState.Data.Loading
    val information = state.details.information
    val tabItems = information.map { it.tabItem }.toImmutableList()
    FixedTabPager(
        modifier = Modifier
            .heightIn(min = 80.dp)
            .shimmer(isShimmering = isLoading),
        isLight = false,
        items = tabItems
    ) { tab ->
        Box(modifier = Modifier.padding(Theme.spacing.spacing16)) {
            when (val item = information[tab]) {
                is InformationUI.Description -> Text(text = item.content)
            }
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
                    shortDescription = "",
                    information = persistentListOf(),
                    variants = persistentListOf(),
                    isSelectionSoldOut = false,
                    colors = persistentListOf(
                        ColorUI(
                            id = "id",
                            type = SwatchType.PlainColor(Color.Cyan, true),
                            index = 0
                        ),
                        ColorUI(
                            id = "id",
                            type = SwatchType.PlainColor(Color.Cyan, true),
                            index = 0
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
                    sizeSectionUI = SizeSectionUI.SingleSize
                )
            ),
            onEvent = { }
        )
    }
}
