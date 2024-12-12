package au.com.alfie.ecomm.designsystem.component.topbar.action

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import au.com.alfie.ecomm.designsystem.animation.DefaultVisibilityAnimation
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScope

@Composable
internal fun TopBarScope.TopBarActions(
    animateVisibility: Boolean = true
) {
    val actions: @Composable () -> Unit = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            state.actions.forEach { topBarAction ->
                topBarAction.component()
            }
        }
    }

    if (animateVisibility) {
        val isSearchOpen = state.isSearchOpen()
        val hasActions = state.actions.isNotEmpty()
        DefaultVisibilityAnimation(isVisible = !isSearchOpen && hasActions) {
            actions()
        }
    } else {
        actions()
    }
}
