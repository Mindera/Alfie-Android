package au.com.alfie.ecomm.designsystem.component.topbar.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import au.com.alfie.ecomm.designsystem.animation.DefaultVisibilityAnimation
import au.com.alfie.ecomm.designsystem.animation.defaultFadeIn
import au.com.alfie.ecomm.designsystem.component.searchbar.SearchTextField
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScope

@Composable
internal fun TopBarScope.SearchHandler(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    enterTransition: EnterTransition = defaultFadeIn(),
    exitTransition: ExitTransition = fadeOut(),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        DefaultVisibilityAnimation(
            isVisible = state.isSearchOpen().not(),
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) {
            content()
        }
        ShowSearchTextField()
    }
}

@Composable
internal fun TopBarScope.ShowSearchTextField() {
    val searchState = state.getSearchState()
    val isSearchOpen = state.isSearchOpen()
    val searchTitle = state.title as? TopBarTitle.Search
    val isScreenWithSearch = searchTitle != null

    BackHandler(isSearchOpen && isScreenWithSearch) {
        (state.title as? TopBarTitle.Search)?.searchState?.invertSearchOpenState()
    }

    val targetState = isSearchOpen || isScreenWithSearch
    DefaultVisibilityAnimation(isVisible = targetState) {
        // We want only to show the enter transition to avoid the content to be reduced due to Actions' sizes
        if (searchState != null) {
            SearchTextField(
                state = searchState,
                isPullDownToRefresh = searchTitle?.isPullDownToRefresh ?: false,
                onTermChange = { searchTitle?.onTermChanged?.invoke(it) },
                onFocusChange = { searchTitle?.onFocusChange?.invoke(it) }
            )
        }
    }
}
