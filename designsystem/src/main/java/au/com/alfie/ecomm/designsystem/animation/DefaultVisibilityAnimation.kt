package au.com.alfie.ecomm.designsystem.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable

@Composable
internal fun DefaultVisibilityAnimation(
    isVisible: Boolean,
    enterTransition: EnterTransition = defaultFadeIn(),
    exitTransition: ExitTransition = fadeOut(),
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = enterTransition,
        exit = exitTransition,
        content = content
    )
}

internal fun defaultFadeIn() = fadeIn(tween(delayMillis = AnimationConstants.DefaultDurationMillis))
