package au.com.alfie.ecomm.designsystem.component.searchbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.R

@Composable
fun rememberSearchState(
    placeholderText: String = stringResource(id = R.string.search_placeholder),
    shouldCloseOnSearchAction: Boolean = true,
    onSearchTermChange: ClickEventOneArg<String> = { }
): SearchState {
    val isSearchOpen = remember { mutableStateOf(false) }
    val searchTerm = remember { mutableStateOf("") }
    val searchTextType = remember { mutableStateOf(SearchTextType.Soft) }

    return remember {
        SearchState(
            onSearchTermChange = onSearchTermChange,
            shouldCloseOnSearchAction = shouldCloseOnSearchAction,
            placeholderText = placeholderText,
            searchTextType = searchTextType,
            isSearchOpen = isSearchOpen,
            searchTerm = searchTerm
        )
    }
}

@Stable
class SearchState internal constructor(
    private val onSearchTermChange: ClickEventOneArg<String>,
    private val shouldCloseOnSearchAction: Boolean,
    val placeholderText: String,
    searchTextType: MutableState<SearchTextType>,
    isSearchOpen: MutableState<Boolean>,
    searchTerm: MutableState<String>
) {
    var isSearchOpen by isSearchOpen
        private set
    var searchTerm by searchTerm
        private set
    var searchTextType by searchTextType
        private set

    private var onSearchTermChangeCustomListener: ClickEventOneArg<String>? = null

    private var onSearchActionListener: ClickEventOneArg<String>? = null

    fun invertSearchOpenState() {
        if (isSearchOpen.not()) updateSearchTerm("")
        isSearchOpen = !isSearchOpen
    }

    fun updateSearchState(isSearchOpen: Boolean) {
        this.isSearchOpen = isSearchOpen
    }

    fun updateSearchType(searchTextType: SearchTextType) {
        this.searchTextType = searchTextType
    }

    fun updateSearchTerm(searchText: String) {
        searchTerm = searchText
        onSearchTermChange(searchText)
        onSearchTermChangeCustomListener?.invoke(searchText)
    }

    fun onSearchAction() {
        if (shouldCloseOnSearchAction) {
            isSearchOpen = false
        }
        onSearchActionListener?.invoke(searchTerm)
    }

    fun setCustomOnSearchTermChange(listener: ClickEventOneArg<String>) {
        onSearchTermChangeCustomListener = listener
    }

    fun setOnSearchAction(listener: ClickEventOneArg<String>) {
        onSearchActionListener = listener
    }
}
