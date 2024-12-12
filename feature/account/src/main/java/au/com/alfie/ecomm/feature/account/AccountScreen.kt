package au.com.alfie.ecomm.feature.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.loading.Loading
import au.com.alfie.ecomm.designsystem.component.loading.LoadingType
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomHostState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.account.component.NavigationButton
import au.com.alfie.ecomm.feature.account.factory.MyAddressBook
import au.com.alfie.ecomm.feature.account.factory.MyDetails
import au.com.alfie.ecomm.feature.account.factory.MyOrders
import au.com.alfie.ecomm.feature.account.factory.SignOut
import au.com.alfie.ecomm.feature.account.factory.Wallet
import au.com.alfie.ecomm.feature.account.factory.Wishlist
import au.com.alfie.ecomm.feature.account.model.AccountEvent
import au.com.alfie.ecomm.feature.account.model.AccountUI
import au.com.alfie.ecomm.feature.account.model.AccountUIState
import au.com.alfie.ecomm.feature.account.model.AccountUIState.Error
import au.com.alfie.ecomm.feature.account.model.AccountUIState.Loaded
import au.com.alfie.ecomm.feature.account.model.AccountUIState.Loading
import au.com.alfie.ecomm.feature.uievent.handleUIEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
internal fun AccountScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel = hiltViewModel<AccountViewModel>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    topBarState.textTopBar(title = stringResource(R.string.account_title_header))
    bottomBarState.hideBottomBar()

    viewModel.handleUIEvents(
        navigator = navigator,
        navController = navController,
        directionProvider = directionProvider,
        snackbarHostState = snackbarHostState
    )

    AccountScreenContent(
        onEvent = viewModel::handleEvent,
        state = viewState
    )
}

@Composable
private fun AccountScreenContent(
    onEvent: ClickEventOneArg<AccountEvent>,
    state: AccountUIState
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        when (state) {
            is Loaded -> AccountScreenLoaded(
                onEvent = onEvent,
                accountUI = state.accountUI
            )
            is Loading -> {
                AccountScreenLoading()
            }
            is Error -> { /* TODO */ }
        }
    }
}

@Composable
private fun AccountScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Loading(
            type = LoadingType.Dark,
            modifier = Modifier.padding(Theme.spacing.spacing12)
        )
    }
}

@Composable
private fun AccountScreenLoaded(
    onEvent: ClickEventOneArg<AccountEvent>,
    accountUI: AccountUI
) {
    LazyColumn {
        items(accountUI.items.size) { index ->
            val item = accountUI.items[index]
            NavigationButton(
                item = item,
                onClickEvent = { onEvent(AccountEvent.OpenEntry(item.uiEvent)) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountScreenPreview() {
    Theme {
        AccountScreenContent(
            onEvent = {},
            state = Loaded(
                accountUI = AccountUI(
                    items = listOf(
                        MyDetails,
                        MyOrders,
                        Wallet,
                        MyAddressBook,
                        Wishlist,
                        SignOut
                    )
                )
            )
        )
    }
}
