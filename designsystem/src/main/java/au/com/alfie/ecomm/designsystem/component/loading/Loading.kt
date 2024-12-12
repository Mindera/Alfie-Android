package au.com.alfie.ecomm.designsystem.component.loading

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme

private val LOADER_DOT_SIZE = 8.dp
private const val LOADER_NUM_DOTS = 3
private const val LOADER_DOT_ANIM_DURATION = 500

@Composable
fun Loading(
    type: LoadingType,
    modifier: Modifier = Modifier,
    numberOfDots: Int = LOADER_NUM_DOTS,
    dotSize: Dp = LOADER_DOT_SIZE,
    dotAnimDuration: Int = LOADER_DOT_ANIM_DURATION
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        repeat(numberOfDots) {
            LoaderDot(
                size = dotSize,
                color = type.color,
                dotAnimDuration = dotAnimDuration,
                initialPeakDelay = it * (dotAnimDuration - (dotAnimDuration / 4)),
                totalDuration = numberOfDots * (dotAnimDuration - (dotAnimDuration / 4))
            )
            if (it < numberOfDots - 1) {
                Spacer(Modifier.width(Theme.spacing.spacing4))
            }
        }
    }
}

@Composable
fun LoadingWithLabel(
    type: LoadingType,
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.loading_label),
    numberOfDots: Int = LOADER_NUM_DOTS,
    dotSize: Dp = LOADER_DOT_SIZE,
    dotAnimDuration: Int = LOADER_DOT_ANIM_DURATION
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Loading(
            type = type,
            numberOfDots = numberOfDots,
            dotSize = dotSize,
            dotAnimDuration = dotAnimDuration
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Text(
            text = label,
            style = Theme.typography.paragraph,
            color = type.color
        )
    }
}

@Composable
private fun LoaderDot(
    size: Dp,
    color: Color,
    dotAnimDuration: Int,
    initialPeakDelay: Int,
    totalDuration: Int,
    minScale: Float = 0.75f,
    minAlpha: Float = 0.2f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dot")
    val scale by infiniteTransition.animateFloatInTimeline(
        minValue = minScale,
        dotAnimDuration = dotAnimDuration,
        totalDuration = totalDuration,
        initialPeakDelay = initialPeakDelay
    )
    val alpha by infiniteTransition.animateFloatInTimeline(
        minValue = minAlpha,
        dotAnimDuration = dotAnimDuration,
        totalDuration = totalDuration,
        initialPeakDelay = initialPeakDelay
    )

    Spacer(
        Modifier
            .size(size)
            .scale(scale)
            .background(
                color = color.copy(alpha = alpha),
                shape = CircleShape
            )
    )
}

@Composable
private fun InfiniteTransition.animateFloatInTimeline(
    minValue: Float,
    dotAnimDuration: Int,
    totalDuration: Int,
    initialPeakDelay: Int
): State<Float> {
    val dotPeakDelay = dotAnimDuration / 2

    return animateFloat(
        initialValue = minValue,
        targetValue = minValue,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                // Total duration means the time to complete the animation of all dots (the animation spectrum)
                durationMillis = totalDuration

                // initialPeakDelay represents the point in the animation spec where the current dot has the maximum
                // scale and opacity values. As the spec includes the animation of all dots ([0; totalDuration[),
                // a keyframe is defined half the dot animation duration (dotPeakDelay) before and after the "peak"
                // (for the rest of the animation spec, the value is 'minValue')
                minValue at initialPeakDelay - dotPeakDelay
                1f at initialPeakDelay
                minValue at initialPeakDelay + dotPeakDelay

                // As the initialPeakDelay can be too close to 0, the previous keyframes are repeated at the end
                // of the animation spectrum to make sure that the animation is smooth
                // This way, any value animated before the beginning of the animation, happen right before the end
                minValue at totalDuration + initialPeakDelay - dotPeakDelay
                1f at totalDuration + initialPeakDelay
                minValue at totalDuration + initialPeakDelay + dotPeakDelay
            }
        ),
        label = "dot"
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFCBCBCB)
@Composable
private fun LoaderPreview() {
    Column(modifier = Modifier.padding(4.dp)) {
        Text(text = "Loader")
        Spacer(modifier = Modifier.height(20.dp))
        Loading(type = LoadingType.Dark)
        Spacer(modifier = Modifier.height(5.dp))
        Loading(type = LoadingType.Light)
        Spacer(modifier = Modifier.height(15.dp))
        LoadingWithLabel(
            type = LoadingType.Dark,
            modifier = Modifier.padding(5.dp)
        )
    }
}
