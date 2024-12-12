package au.com.alfie.ecomm.designsystem.component.topbar.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.test.SEARCH_BACK_BUTTON
import au.com.alfie.ecomm.designsystem.animation.DefaultVisibilityAnimation
import au.com.alfie.ecomm.designsystem.animation.defaultFadeIn
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScope
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
internal fun TopBarScope.NavigationIcon(
    onNavigationClick: ClickEvent
) {
    val hasNavigationWithoutSearchState = state.showNavigationIcon && state.title !is TopBarTitle.Search

    if (hasNavigationWithoutSearchState) {
        DefaultNavigationIcon(onNavigationClick = onNavigationClick)
    } else {
        SearchNavigationIcon()
    }
}

@Composable
internal fun TopBarScope.DefaultNavigationIcon(
    onNavigationClick: ClickEvent,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    enterTransition: EnterTransition = defaultFadeIn(),
    exitTransition: ExitTransition = ExitTransition.None
) {
    DefaultVisibilityAnimation(
        isVisible = isVisible,
        enterTransition = enterTransition,
        exitTransition = exitTransition
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                modifier = Modifier.size(Theme.iconSize.large),
                onClick = {
                    if (state.isSearchOpen()) {
                        state.getSearchState()?.invertSearchOpenState()
                    } else {
                        onNavigationClick()
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.size(Theme.iconSize.medium),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TopBarScope.SearchNavigationIcon() {
    AnimatedContent(
        targetState = state.isSearchOpen(),
        transitionSpec = {
            fadeIn() togetherWith fadeOut(tween(delayMillis = AnimationConstants.DefaultDurationMillis))
        },
        label = "TopBarNavigationAnimation"
    ) {
        // Only show content after it reaches the final size to avoid being cut during animation
        val alpha = if (!this.transition.isRunning) 1F else 0F
        if (it) {
            IconButton(
                modifier = Modifier
                    .size(Theme.iconSize.large)
                    .graphicsLayer { this.alpha = alpha }
                    .testTag(SEARCH_BACK_BUTTON),
                onClick = { state.getSearchState()?.invertSearchOpenState() }
            ) {
                Icon(
                    modifier = Modifier.size(Theme.iconSize.medium),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        } else if (state.title is TopBarTitle.Search) {
            // TopAppBar applies an horizontal adding internally only when a navigation icon is present and can't be changed.
            // To prevent the paddings from being applied later causing changes in the content a minimum space is required
            // for the navigation icon content.
            Spacer(modifier = Modifier.width(Theme.spacing.spacing12))
        }
    }
}
