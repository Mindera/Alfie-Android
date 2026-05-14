package com.mindera.alfie.designsystem.component.topbar.action

import androidx.compose.runtime.Composable
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.component.searchbar.SearchState

sealed class TopBarAction private constructor(
    internal val component: @Composable () -> Unit
) {
    data class Account(
        val onClick: ClickEvent
    ) : TopBarAction(
        component = {
            AccountTopBarAction(onClick = onClick)
        }
    )

    data class Debug(
        val onClick: ClickEvent
    ) : TopBarAction(
        component = {
            DebugTopBarAction(onClick = onClick)
        }
    )

    data class Search(
        val searchState: SearchState
    ) : TopBarAction(
        component = {
            SearchTopBarAction(onClick = searchState::invertSearchOpenState)
        }
    )

    data class Share(
        val onClick: ClickEvent
    ) : TopBarAction(
        component = {
            ShareTopBarAction(onClick = onClick)
        }
    )

    data class WishList(
        val onClick: ClickEvent
    ) : TopBarAction(
        component = {
            WishlistTopBarAction(onClick = onClick)
        }
    )
}
