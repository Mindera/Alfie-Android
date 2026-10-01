package com.mindera.alfie.feature.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.navigation.arguments.wishlist.WishlistNavArgs
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.core.ui.test.WISHLIST_EMPTY_STATE
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.ProductCard
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomHostState
import com.mindera.alfie.designsystem.component.state.EmptyState
import com.mindera.alfie.designsystem.component.state.StateMessage
import com.mindera.alfie.designsystem.component.state.StateMessageAction
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.action.TopBarAction
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.uievent.handleUIEvents
import com.mindera.alfie.feature.wishlist.models.WishlistProductUi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.mindera.alfie.designsystem.R as DesignR

private const val GRID_COLUMNS = 2

// Two rows' worth, enough to fill the space a typical wishlist occupies while it loads.
private const val LOADING_PLACEHOLDER_COUNT = 4

@Destination(navArgsDelegate = WishlistNavArgs::class)
@Composable
internal fun WishlistScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel: WishlistViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // TODO: sharing a wishlist has no defined behaviour yet. The icon is shown because the design
    //  has it; wire it up once what gets shared (a link, the product list…) is decided.
    val actions = persistentListOf(TopBarAction.Share(onClick = { }))
    // As a root tab there is nothing to go back to — navigateUp() would pop the shopper out of the
    // tab — so the navigation icon only shows when the screen was pushed from elsewhere.
    topBarState.textTopBar(
        title = stringResource(id = DesignR.string.wishlist_screen_title),
        showNavigationIcon = viewModel.launchFromTop.not(),
        isLeftAligned = false,
        actions = actions
    )
    if (viewModel.launchFromTop) {
        bottomBarState.showBottomBar()
    } else {
        bottomBarState.hideBottomBar()
    }

    viewModel.handleUIEvents(
        navigator = navigator,
        navController = navController,
        directionProvider = directionProvider,
        snackbarHostState = snackbarHostState
    )

    WishlistScreenContent(
        state = state,
        onRetryClick = viewModel::onRetry
    )
}

@Composable
private fun WishlistScreenContent(
    state: WishlistUiState,
    onRetryClick: ClickEvent
) {
    when (state) {
        is WishlistUiState.Data.Loading -> WishlistLoading()
        is WishlistUiState.Data.Loaded -> {
            if (state.wishlist.isEmpty()) {
                EmptyState(
                    message = stringResource(R.string.wishlist_empty_message),
                    subtitle = stringResource(R.string.wishlist_empty_subtitle),
                    icon = AlfieIcons.Wishlist,
                    modifier = Modifier.testTag(WISHLIST_EMPTY_STATE)
                )
            } else {
                WishlistGrid(wishlist = state.wishlist)
            }
        }
        is WishlistUiState.Error -> StateMessage(
            title = stringResource(R.string.wishlist_error_message),
            action = StateMessageAction(
                label = stringResource(R.string.wishlist_error_retry),
                onClick = onRetryClick
            )
        )
    }
}

@Composable
private fun WishlistGrid(
    wishlist: List<WishlistProductUi>
) {
    WishlistGridLayout {
        items(wishlist) { item ->
            // Everything on this screen is wishlisted, so the heart is always filled.
            ProductCard(
                productCardType = item.productCardData,
                isWishlisted = true
            )
        }
    }
}

@Composable
private fun WishlistLoading() {
    WishlistGridLayout {
        items(LOADING_PLACEHOLDER_COUNT) {
            ProductCard(
                productCardType = loadingPlaceholder,
                isLoading = true
            )
        }
    }
}

/**
 * The Figma `Vertical Product List`: two columns inside the screen margin, an 8 dp gutter between
 * them and 16 dp between rows, starting flush under the header.
 */
@Composable
private fun WishlistGridLayout(
    content: LazyGridScope.() -> Unit
) {
    val theme = LocalTheme.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_COLUMNS),
        contentPadding = PaddingValues(horizontal = theme.spacing.spacing16),
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing8),
        verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing16),
        modifier = Modifier.fillMaxSize(),
        content = content
    )
}

private val loadingPlaceholder = ProductCardType.Vertical(
    image = ImageUI(images = persistentListOf(), alt = ""),
    brand = "",
    name = "",
    price = PriceType.Default(price = "")
)

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 640)
@Composable
private fun WishlistScreenLoadedPreview() {
    Theme {
        WishlistScreenContent(
            state = WishlistUiState.Data.Loaded(previewWishlist),
            onRetryClick = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 640)
@Composable
private fun WishlistScreenEmptyPreview() {
    Theme {
        WishlistScreenContent(
            state = WishlistUiState.Data.Loaded(persistentListOf()),
            onRetryClick = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 640)
@Composable
private fun WishlistScreenLoadingPreview() {
    Theme {
        WishlistScreenContent(
            state = WishlistUiState.Data.Loading,
            onRetryClick = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 640)
@Composable
private fun WishlistScreenErrorPreview() {
    Theme {
        WishlistScreenContent(
            state = WishlistUiState.Error,
            onRetryClick = { }
        )
    }
}

private val previewWishlist: ImmutableList<WishlistProductUi> = persistentListOf(
    WishlistProductUi(
        productCardData = ProductCardType.Vertical(
            image = ImageUI(images = persistentListOf(), alt = ""),
            brand = "Brand Name",
            name = "100% Cotton Fluid Blazer",
            price = PriceType.Default(price = "£170"),
            onFavoriteClick = { },
            addToBagClick = { }
        )
    ),
    WishlistProductUi(
        productCardData = ProductCardType.Vertical(
            image = ImageUI(images = persistentListOf(), alt = ""),
            brand = "Brand Name",
            name = "100% Cotton Fluid Blazer",
            price = PriceType.Default(price = "£170"),
            onFavoriteClick = { },
            addToBagClick = { }
        )
    )
)
