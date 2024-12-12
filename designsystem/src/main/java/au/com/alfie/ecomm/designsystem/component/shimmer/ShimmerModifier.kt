package au.com.alfie.ecomm.designsystem.component.shimmer

import androidx.annotation.FloatRange
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonSize
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.coroutines.delay

private const val ALPHA_DURATION = 1000
private const val BACKGROUND_DURATION = 500

@Composable
fun Modifier.shimmer(
    isShimmering: Boolean,
    colors: ShimmerColors = ShimmerColors.Light,
    shape: Shape = Theme.shape.none,
    minWidth: Dp = Dp.Unspecified,
    minHeight: Dp = Dp.Unspecified,
    xScale: Float = Theme.scale.scale100,
    yScale: Float = Theme.scale.scale100
): Modifier {
    val contentAlphaState by contentAlpha(isShimmering)
    val contentBackgroundState by contentBackground(isShimmering, colors.colors.first())

    return if (isShimmering) {
        val shimmerBrush = rememberShimmerBrush(colors = colors.colors)

        this then Modifier
            .defaultMinSize(minWidth, minHeight)
            .clip(shape)
            .clipToBounds()
            .drawWithContent {
                with(shimmerBrush) {
                    drawShimmer(
                        shimmerSize = size.copy(
                            width = size.width * xScale,
                            height = size.height * yScale
                        )
                    )
                }
            }
    } else {
        this then Modifier
            .clip(shape)
            .clipToBounds()
            .drawBehind {
                drawRect(
                    color = contentBackgroundState,
                    size = size.copy(
                        width = size.width * xScale,
                        height = size.height * yScale
                    )
                )
            }
            .graphicsLayer { alpha = contentAlphaState }
    }
}

@Composable
fun Modifier.shimmer(
    isShimmering: Boolean,
    lines: Int,
    lineHeight: TextUnit,
    lineMargin: Dp = Theme.spacing.spacing4,
    @FloatRange(
        from = 0.0,
        to = 1.0,
        fromInclusive = false
    ) lastLineFraction: Float = 1f,
    colors: ShimmerColors = ShimmerColors.Light,
    cornerRadius: Dp = Dp.Unspecified
): Modifier {
    val contentAlphaState by contentAlpha(isShimmering)
    val contentBackgroundState by contentBackground(isShimmering, colors.colors.first())

    val density = LocalDensity.current
    val marginHeightPx = remember { with(density) { lineMargin.toPx() } }
    val lineHeightPx = remember { with(density) { lineHeight.toPx() } }
    val drawingHeight = remember {
        with(density) {
            (lineHeightPx * lines + marginHeightPx * (lines - 1)).toDp()
        }
    }
    val cornerRadiusPx = remember { with(density) { cornerRadius.toPx() } }

    return if (isShimmering) {
        val shimmerBrush = rememberShimmerBrush(colors = colors.colors)

        this then Modifier
            .height(drawingHeight)
            .drawWithContent {
                drawLines(
                    lines = lines,
                    lineHeightPx = lineHeightPx,
                    marginHeightPx = marginHeightPx,
                    lastLineFraction = lastLineFraction
                ) { lineOffset, size ->
                    with(shimmerBrush) {
                        drawShimmerLine(
                            lineOffset = lineOffset,
                            shimmerSize = size,
                            cornerRadius = cornerRadiusPx
                        )
                    }
                }
            }
    } else {
        this then Modifier
            .drawBehind {
                drawLines(
                    lines = lines,
                    lineHeightPx = lineHeightPx,
                    marginHeightPx = marginHeightPx,
                    lastLineFraction = lastLineFraction
                ) { lineOffset, size ->
                    drawRoundRect(
                        color = contentBackgroundState,
                        cornerRadius = CornerRadius(cornerRadiusPx),
                        topLeft = Offset(0f, lineOffset),
                        size = size
                    )
                }
            }
            .graphicsLayer { alpha = contentAlphaState }
    }
}

private fun DrawScope.drawLines(
    lines: Int,
    lineHeightPx: Float,
    marginHeightPx: Float,
    lastLineFraction: Float,
    draw: (lineOffset: Float, size: Size) -> Unit
) {
    for (i in 0..<lines) {
        val lineOffset = lineHeightPx * i + (marginHeightPx * i)
        val width = if (i == lines - 1) {
            size.width * lastLineFraction
        } else {
            size.width
        }

        draw(
            lineOffset,
            size.copy(
                width = width,
                height = lineHeightPx
            )
        )
    }
}

@Composable
private fun contentAlpha(isShimmering: Boolean) = animateFloatAsState(
    targetValue = if (isShimmering) 0f else 1f,
    animationSpec = tween(durationMillis = ALPHA_DURATION, easing = EaseIn),
    label = "content alpha"
)

@Composable
private fun contentBackground(
    isShimmering: Boolean,
    color: Color
) = animateColorAsState(
    targetValue = if (isShimmering) color else Color.Transparent,
    animationSpec = tween(durationMillis = BACKGROUND_DURATION, easing = EaseOut),
    label = "content background"
)

@Preview(showBackground = true)
@Composable
private fun PreviewShimmerModifier() {
    var isShimmerEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            isShimmerEnabled = !isShimmerEnabled
        }
    }

    Column {
        Button(
            type = ButtonType.Primary,
            text = "Primary",
            buttonSize = ButtonSize.Small,
            isShimmering = isShimmerEnabled,
            onClick = { }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Button(
            type = ButtonType.Secondary,
            text = "Secondary",
            buttonSize = ButtonSize.Small,
            isShimmering = isShimmerEnabled,
            onClick = { }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Button(
            type = ButtonType.Tertiary,
            text = "Tertiary",
            buttonSize = ButtonSize.Small,
            isShimmering = isShimmerEnabled,
            onClick = { }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Button(
            type = ButtonType.Underlined,
            text = "Underlined",
            buttonSize = ButtonSize.Small,
            isShimmering = isShimmerEnabled,
            onClick = { }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            text = "O rato roeu a roupa do rei de Roma",
            modifier = Modifier.shimmer(isShimmerEnabled)
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras sagittis, leo nec ultricies sodales, " +
                "tellus diam bibendum erat, vel scelerisque augue magna ut arcu. Nulla eu lacinia leo.",
            style = Theme.typography.paragraph,
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(
                    isShimmering = isShimmerEnabled,
                    lineHeight = Theme.typography.paragraph.lineHeight,
                    lines = 4,
                    lastLineFraction = .75f,
                    cornerRadius = 4.dp
                )
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Image(
            painter = painterResource(id = R.drawable.ic_action_heart_fill),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .shimmer(isShimmerEnabled)
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.Red, shape = CircleShape)
                .shimmer(
                    isShimmering = isShimmerEnabled,
                    shape = CircleShape
                )
        )
    }
}
