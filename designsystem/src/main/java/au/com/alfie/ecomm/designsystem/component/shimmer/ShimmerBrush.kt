package au.com.alfie.ecomm.designsystem.component.shimmer

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private const val DEFAULT_DURATION = 1000
private const val GRADIENT_DEGREE = 45
private const val DEGREE_180 = 180

@Composable
internal fun rememberShimmerBrush(
    colors: ImmutableList<Color>,
    duration: Int = DEFAULT_DURATION
): ShimmerBrush {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenSize = remember {
        with(density) {
            Size(
                width = configuration.screenWidthDp.dp.toPx(),
                height = configuration.screenHeightDp.dp.toPx()
            )
        }
    }

    val shimmerBrush = remember {
        ShimmerBrush(
            colors = colors,
            duration = duration,
            screenSize = screenSize
        )
    }

    LaunchedEffect(colors) {
        shimmerBrush.startAnimation()
    }

    return shimmerBrush
}

@Stable
internal class ShimmerBrush(
    private val colors: ImmutableList<Color>,
    private val duration: Int,
    private val screenSize: Size
) {
    private val transitionAnimation = Animatable(0f)
    private val gradientStart: Offset
    private val gradientEnd: Offset

    init {
        val gradientOffset = rotateGradient(screenSize)
        gradientStart = gradientOffset.first
        gradientEnd = gradientOffset.second
    }

    suspend fun startAnimation() {
        transitionAnimation.animateTo(
            targetValue = screenSize.width,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = duration,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    fun ContentDrawScope.drawShimmer(shimmerSize: Size) {
        drawRect(
            brush = brush(transitionAnimation.value),
            size = shimmerSize
        )
    }

    fun ContentDrawScope.drawShimmerLine(
        lineOffset: Float,
        shimmerSize: Size,
        cornerRadius: Float
    ) {
        drawRoundRect(
            brush = brush(transitionAnimation.value),
            cornerRadius = CornerRadius(cornerRadius),
            topLeft = Offset(0f, lineOffset),
            size = shimmerSize
        )
    }

    private fun brush(transitionState: Float) = Brush.linearGradient(
        colors = colors,
        start = Offset(gradientStart.x + transitionState, gradientStart.y + transitionState),
        end = Offset(gradientEnd.x + transitionState, gradientEnd.y + transitionState),
        tileMode = TileMode.Mirror
    )
}

private fun rotateGradient(size: Size): Pair<Offset, Offset> {
    val (x, y) = size
    val gamma = atan2(y, x)

    if (gamma == 0f || gamma == (PI / 2).toFloat()) {
        // degenerate rectangle
        return Offset.Zero to Offset.Zero
    }

    val alpha = (GRADIENT_DEGREE * PI / DEGREE_180).toFloat()

    val gradientLength = x / cos(alpha)

    val centerOffsetX = cos(alpha) * gradientLength / 2
    val centerOffsetY = sin(alpha) * gradientLength / 2

    val start = Offset(size.center.x - centerOffsetX, size.center.y - centerOffsetY)
    val end = Offset(size.center.x + centerOffsetX, size.center.y + centerOffsetY)

    return start to end
}

@Composable
@Preview
private fun PreviewShimmerBrush() {
    val shimmerBrush = rememberShimmerBrush(
        colors = persistentListOf(
            Theme.color.primary.mono600,
            Theme.color.primary.mono900,
            Theme.color.primary.mono600
        )
    )
    Box(
        modifier = Modifier
            .size(420.dp, 320.dp)
            .drawWithContent {
                with(shimmerBrush) {
                    drawShimmer(size)
                }
            }
    )
}
