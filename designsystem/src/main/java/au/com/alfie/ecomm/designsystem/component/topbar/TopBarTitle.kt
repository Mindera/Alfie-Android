package au.com.alfie.ecomm.designsystem.component.topbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.searchbar.SearchState
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScope

@Stable
sealed interface TopBarTitle {

    sealed interface Searchable : TopBarTitle {
        val searchState: SearchState?
    }

    data class Text(
        val title: String,
        val isLeftAligned: Boolean = false
    ) : TopBarTitle {
        companion object {
            val EMPTY = Text("")
        }
    }

    data class Icon(
        @DrawableRes val icon: Int,
        val contentDescription: String? = null
    ) : TopBarTitle {
        companion object {
            val EMPTY = Icon(-1)
        }
    }

    data class Search(
        override val searchState: SearchState,
        val isPullDownToRefresh: Boolean,
        val onTermChanged: ClickEventOneArg<String>,
        val onFocusChange: ClickEventOneArg<Boolean>,
        val customOverlay: (@Composable (
            padding: PaddingValues,
            appContent: @Composable (PaddingValues) -> Unit
        ) -> Unit)?
    ) : Searchable

    data class Custom(
        override val searchState: SearchState? = null,
        val custom: @Composable TopBarScope.() -> Unit
    ) : Searchable
}
