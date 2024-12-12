package au.com.alfie.ecomm.designsystem.component.listitem

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import au.com.alfie.ecomm.core.commons.extension.nextFloat
import au.com.alfie.ecomm.designsystem.component.shimmer.shimmer
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlin.random.Random

private const val ENTER_TRANSITION_DURATION = 1000
private const val EXIT_TRANSITION_DURATION = 1000

@Composable
fun ListItemWithShimmering(
    headlineContent: @Composable (Modifier) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    scale: Float = remember { Random.nextFloat(from = Theme.scale.scale40, until = Theme.scale.scale60) },
    overlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation
) {
    ListItem(
        modifier = modifier,
        overlineContent = overlineContent,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        colors = colors,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        headlineContent = {
            AnimatedContent(
                targetState = isLoading,
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
            ) { isLoading ->
                val modifierShimmer = Modifier
                    .fillMaxWidth()
                    .shimmer(
                        isShimmering = isLoading,
                        xScale = scale
                    )

                headlineContent(modifierShimmer)
            }
        }
    )
}
