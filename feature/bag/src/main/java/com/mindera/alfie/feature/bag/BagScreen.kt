package com.mindera.alfie.feature.bag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.core.ui.test.BAG_EMPTY_STATE
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.divider.DividerType
import com.mindera.alfie.designsystem.component.divider.HorizontalDivider
import com.mindera.alfie.designsystem.component.inlinemessage.InlineMessage
import com.mindera.alfie.designsystem.component.productcard.ProductCard
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomHostState
import com.mindera.alfie.designsystem.component.state.EmptyState
import com.mindera.alfie.designsystem.component.state.StateMessage
import com.mindera.alfie.designsystem.component.state.StateMessageAction
import com.mindera.alfie.designsystem.component.swipe.SwipeAction
import com.mindera.alfie.designsystem.component.swipe.SwipeActionType
import com.mindera.alfie.designsystem.component.swipe.SwipeActions
import com.mindera.alfie.designsystem.component.swipe.rememberSwipeActionsState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.bag.component.BagSummary
import com.mindera.alfie.feature.bag.models.BagContentUi
import com.mindera.alfie.feature.bag.models.BagItemNotice
import com.mindera.alfie.feature.bag.models.BagProductUi
import com.mindera.alfie.feature.uievent.handleUIEvents
import com.mindera.alfie.repository.bag.BagProduct
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.persistentListOf

// Enough rows to fill the space a typical bag occupies, so the screen does not jump when the data
// lands.
private const val LOADING_PLACEHOLDER_COUNT = 3

@Destination
@Composable
internal fun BagScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel: BagViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // No actions: Wishlist and Account are both in the bottom bar, so nothing becomes unreachable.
    // No navigation icon either — Bag is only ever reached as a root tab, so navigateUp() would pop
    // the user out of the tab rather than go back.
    topBarState.textTopBar(
        title = stringResource(R.string.bag_screen_title),
        showNavigationIcon = false,
        isLeftAligned = false,
        actions = persistentListOf()
    )
    bottomBarState.showBottomBar()

    viewModel.handleUIEvents(
        navigator = navigator,
        navController = navController,
        directionProvider = directionProvider,
        snackbarHostState = snackbarHostState
    )

    BagScreenContent(
        state = state,
        onRemoveClick = viewModel::onRemoveClicked,
        onSaveClick = viewModel::onSaveClicked,
        onRetryClick = viewModel::onRetry
    )
}

@Composable
private fun BagScreenContent(
    state: BagUiState,
    onRemoveClick: ClickEventOneArg<BagProduct>,
    onSaveClick: ClickEventOneArg<BagProduct>,
    onRetryClick: ClickEvent
) {
    when (state) {
        is BagUiState.Data.Loaded -> BagList(
            content = state.content,
            onRemoveClick = onRemoveClick,
            onSaveClick = onSaveClick
        )
        is BagUiState.Data.Loading -> BagLoading()
        is BagUiState.Data.Empty -> EmptyState(
            message = stringResource(R.string.bag_empty_message),
            icon = AlfieIcons.Bag,
            modifier = Modifier.testTag(BAG_EMPTY_STATE)
        )
        is BagUiState.Error -> StateMessage(
            title = stringResource(R.string.bag_error_message),
            action = StateMessageAction(
                label = stringResource(R.string.bag_error_retry),
                onClick = onRetryClick
            )
        )
    }
}

@Composable
private fun BagList(
    content: BagContentUi,
    onRemoveClick: ClickEventOneArg<BagProduct>,
    onSaveClick: ClickEventOneArg<BagProduct>
) {
    val theme = LocalTheme.current
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            // No horizontal content padding: the swipe panels have to reach the screen edge, so the
            // margin is applied to each row's content instead.
            verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing8),
            modifier = Modifier.weight(1f)
        ) {
            content.items.forEachIndexed { index, item ->
                item(key = item.id) {
                    BagLineItem(
                        item = item,
                        onRemoveClick = onRemoveClick,
                        onSaveClick = onSaveClick
                    )
                }
                if (index < content.items.lastIndex) {
                    item(key = "divider-${item.id}") {
                        HorizontalDivider(
                            dividerType = DividerType.Solid1Mono200,
                            modifier = Modifier.padding(horizontal = theme.spacing.spacing16)
                        )
                    }
                }
            }
        }
        // Checkout is deferred to the Themed web surfaces story and the app has no checkout
        // destination yet, so the CTA is disabled rather than left enabled and inert — a primary
        // button that silently does nothing on tap is worse than one that reads as unavailable.
        BagSummary(
            summary = content.summary,
            onContinueClick = { },
            isContinueEnabled = false
        )
    }
}

/** One bag row: the product card, its stock notice, and the actions revealed by swiping. */
@Composable
private fun BagLineItem(
    item: BagProductUi,
    onRemoveClick: ClickEventOneArg<BagProduct>,
    onSaveClick: ClickEventOneArg<BagProduct>
) {
    val theme = LocalTheme.current
    val swipeState = rememberSwipeActionsState()
    val productName = item.productCardData.name

    val saveLabel = stringResource(R.string.bag_item_save)
    val removeLabel = stringResource(R.string.bag_item_remove)
    val saveDescription = stringResource(R.string.bag_item_save_a11y, productName)
    val removeDescription = stringResource(R.string.bag_item_remove_a11y, productName)

    // onRemoveClick/onSaveClick are deliberately not keys: bound method references are new
    // objects on every recomposition, which would defeat the memoisation for no behaviour gain.
    val actions = remember(item.bagProduct, saveLabel, removeLabel, saveDescription, removeDescription) {
        persistentListOf(
            SwipeAction(
                icon = AlfieIcons.Wishlist,
                label = saveLabel,
                contentDescription = saveDescription,
                onClick = { onSaveClick(item.bagProduct) }
            ),
            SwipeAction(
                icon = AlfieIcons.Close,
                label = removeLabel,
                contentDescription = removeDescription,
                type = SwipeActionType.Destructive,
                onClick = { onRemoveClick(item.bagProduct) }
            )
        )
    }
    // The overflow button gives the same two actions a tap path — swipe-only actions are out of
    // reach for switch-access and keyboard users. It toggles so that path can also dismiss the row
    // without committing to Save or Remove.
    val card = remember(item.productCardData, swipeState) {
        item.productCardData.copy(onOverflowClick = swipeState::toggle)
    }

    SwipeActions(
        actions = actions,
        state = swipeState
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing4),
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.color.surface.backgroundPrimary)
                .padding(horizontal = theme.spacing.spacing16)
        ) {
            ProductCard(productCardType = card)
            item.notice?.let { notice ->
                InlineMessage(message = notice.message())
            }
        }
    }
}

@Composable
private fun BagItemNotice.message(): String = when (this) {
    is BagItemNotice.LowStock -> pluralStringResource(
        id = R.plurals.bag_item_low_stock,
        count = remaining,
        remaining
    )
    is BagItemNotice.Unavailable -> stringResource(R.string.bag_item_unavailable)
}

@Composable
private fun BagLoading() {
    val theme = LocalTheme.current
    Column(
        verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing8),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = theme.spacing.spacing16)
    ) {
        repeat(LOADING_PLACEHOLDER_COUNT) {
            ProductCard(
                productCardType = loadingPlaceholderCard,
                isLoading = true
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 640)
@Composable
private fun BagScreenLoadedPreview() {
    Theme {
        BagScreenContent(
            state = BagUiState.Data.Loaded(previewBagContent),
            onRemoveClick = {},
            onSaveClick = {},
            onRetryClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 640)
@Composable
private fun BagScreenSingleItemPreview() {
    Theme {
        BagScreenContent(
            state = BagUiState.Data.Loaded(previewSingleItemBagContent),
            onRemoveClick = {},
            onSaveClick = {},
            onRetryClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 640)
@Composable
private fun BagScreenEmptyPreview() {
    Theme {
        BagScreenContent(
            state = BagUiState.Data.Empty,
            onRemoveClick = {},
            onSaveClick = {},
            onRetryClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 640)
@Composable
private fun BagScreenLoadingPreview() {
    Theme {
        BagScreenContent(
            state = BagUiState.Data.Loading,
            onRemoveClick = {},
            onSaveClick = {},
            onRetryClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 640)
@Composable
private fun BagScreenErrorPreview() {
    Theme {
        BagScreenContent(
            state = BagUiState.Error,
            onRemoveClick = {},
            onSaveClick = {},
            onRetryClick = {}
        )
    }
}
