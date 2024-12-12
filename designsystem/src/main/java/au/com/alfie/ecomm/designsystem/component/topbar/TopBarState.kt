package au.com.alfie.ecomm.designsystem.component.topbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.searchbar.SearchState
import au.com.alfie.ecomm.designsystem.component.topbar.action.TopBarAction
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun rememberTopBarState(
    title: TopBarTitle = TopBarTitle.Icon(R.drawable.ic_alfie_logo_dark),
    showNavigationIcon: Boolean = false,
    isDarkTheme: Boolean = false,
    isVisible: Boolean = true,
    actions: ImmutableList<TopBarAction> = persistentListOf()
): TopBarState = remember {
    TopBarState(
        title = title,
        showNavigationIcon = showNavigationIcon,
        isDarkTheme = isDarkTheme,
        isVisible = isVisible,
        actions = actions
    )
}

@Stable
class TopBarState(
    title: TopBarTitle,
    showNavigationIcon: Boolean,
    isDarkTheme: Boolean = false,
    isVisible: Boolean = true,
    actions: ImmutableList<TopBarAction> = persistentListOf()
) {
    var title by mutableStateOf(title)
        private set

    var showNavigationIcon by mutableStateOf(showNavigationIcon)
        private set

    var isDarkTheme by mutableStateOf(isDarkTheme)
        private set

    var actions by mutableStateOf(actions)
        private set

    var isVisible by mutableStateOf(isVisible)
        private set

    fun textTopBar(
        title: String,
        showNavigationIcon: Boolean = true,
        isDarkTheme: Boolean = false,
        isLeftAligned: Boolean = false,
        actions: ImmutableList<TopBarAction> = persistentListOf()
    ) {
        this.title = TopBarTitle.Text(
            title = title,
            isLeftAligned = isLeftAligned
        )
        this.showNavigationIcon = showNavigationIcon
        this.isDarkTheme = isDarkTheme
        this.actions = actions
        this.isVisible = true
    }

    fun logoTopBar(
        @DrawableRes icon: Int = R.drawable.ic_alfie_logo_dark,
        showNavigationIcon: Boolean = false,
        actions: ImmutableList<TopBarAction> = persistentListOf()
    ) {
        this.title = TopBarTitle.Icon(icon)
        this.showNavigationIcon = showNavigationIcon
        this.actions = actions
        this.isVisible = true
    }

    fun searchTopBar(
        searchState: SearchState,
        isPullDownRefresh: Boolean,
        showNavigationIcon: Boolean = true,
        onTermChanged: ClickEventOneArg<String>,
        onFocusChange: ClickEventOneArg<Boolean>,
        customOverlay: (@Composable (
            padding: PaddingValues,
            appContent: @Composable (PaddingValues) -> Unit
        ) -> Unit)? = null
    ) {
        this.title = TopBarTitle.Search(
            isPullDownToRefresh = isPullDownRefresh,
            searchState = searchState,
            onTermChanged = onTermChanged,
            onFocusChange = onFocusChange,
            customOverlay = customOverlay
        )
        this.showNavigationIcon = showNavigationIcon
        this.actions = persistentListOf()
        this.isVisible = true
    }

    fun customTopBar(
        searchState: SearchState? = null,
        actions: ImmutableList<TopBarAction> = persistentListOf(),
        showNavigationIcon: Boolean = false,
        isDarkTheme: Boolean = false,
        content: @Composable TopBarScope.() -> Unit
    ) {
        this.title = TopBarTitle.Custom(searchState, content)
        this.showNavigationIcon = showNavigationIcon
        this.isDarkTheme = isDarkTheme
        this.isVisible = true
        this.actions = actions
    }

    fun hideTopBar() {
        this.isVisible = false
    }

    fun isSearchOpen(): Boolean =
        actions.filterIsInstance<TopBarAction.Search>().firstOrNull()?.searchState?.isSearchOpen
            ?: (title as? TopBarTitle.Searchable)?.searchState?.isSearchOpen
            ?: false

    fun getSearchState(): SearchState? =
        actions.filterIsInstance<TopBarAction.Search>().firstOrNull()?.searchState
            ?: (title as? TopBarTitle.Searchable)?.searchState
}
