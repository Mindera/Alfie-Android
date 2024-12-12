package au.com.alfie.ecomm.core.ui.extension

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable

private const val ENTER_TRANSITION_DURATION = 1000
private const val EXIT_TRANSITION_DURATION = 1000

@Composable
fun LazyItemScope.ItemWithUpdate(
    targetState: Boolean,
    content: @Composable (Boolean) -> Unit
) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            fadeIn(
                tween(
                    durationMillis = ENTER_TRANSITION_DURATION,
                    easing = EaseIn
                )
            ) togetherWith fadeOut(
                tween(
                    durationMillis = EXIT_TRANSITION_DURATION,
                    easing = EaseOut
                )
            )
        },
        label = "item content animation"
    ) { isPlaceholder ->
        content(isPlaceholder)
    }
}
