package com.mindera.alfie.designsystem.component.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

/**
 * Two-handle range slider, per Design System node `3450:19485`.
 *
 * The DS component has **no variants**: there is no disabled, pressed or focused state, no
 * single-thumb variant, and no step/tick model — the track is continuous. Don't add any of those
 * here without a DS change.
 *
 * Two deviations are deliberate and worth knowing about:
 * - The DS leaves the **inactive track unfilled** (its fill is genuinely unbound, so it renders
 *   invisible as authored). The PLP mock shows it as `border/soft`, which is what is used here.
 *   Confirm with design and bind the DS fill.
 * - The DS documents the thumbs as a **24 dp touch target**, under Android's 48 dp minimum. The thumb
 *   is nonetheless left at 24 dp: Material measures this slot to inset the track, so padding it out
 *   would shorten the rail and pull the handles inward, away from the ends the DS draws them at.
 *   Material applies its own minimum interactive size to the thumb slot; verify by hand and raise
 *   with design if the target still feels tight.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null
) {
    val theme = LocalTheme.current
    RangeSlider(
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        valueRange = valueRange,
        modifier = modifier.fillMaxWidth(),
        startThumb = { SliderThumb() },
        endThumb = { SliderThumb() },
        track = { state -> SliderTrack(state = state, valueRange = valueRange) },
        colors = SliderDefaults.colors(
            // Material draws its own track unless `track` is overridden; these remain as a
            // belt-and-braces fallback so a future Material change cannot reintroduce purple.
            activeTrackColor = theme.color.surface.foregroundInvertedPrimary,
            inactiveTrackColor = theme.color.border.soft
        )
    )
}

/**
 * White disc with a 1 px `surface/foreground-inverted-primary` ring and the `Shadow-Sheer` drop
 * shadow (5% black, y-offset 1, blur 2).
 */
@Composable
private fun SliderThumb() {
    val theme = LocalTheme.current
    Box(
        modifier = Modifier
            .size(theme.sizing.icon.medium)
            .shadow(
                elevation = THUMB_SHADOW_ELEVATION,
                shape = Theme.shape.full
            )
            .background(
                color = theme.color.surface.backgroundPrimary,
                shape = Theme.shape.full
            )
            .border(
                width = theme.primitive.border.weightDefault,
                color = theme.color.surface.foregroundInvertedPrimary,
                shape = Theme.shape.full
            )
    )
}

/** Both tracks are 2 dp: inactive `border/soft`, active `surface/foreground-inverted-primary`. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SliderTrack(
    state: RangeSliderState,
    valueRange: ClosedFloatingPointRange<Float>
) {
    val theme = LocalTheme.current
    val span = (valueRange.endInclusive - valueRange.start).takeIf { it > 0f } ?: 1f
    val startFraction = ((state.activeRangeStart - valueRange.start) / span).coerceIn(0f, 1f)
    val endFraction = ((state.activeRangeEnd - valueRange.start) / span).coerceIn(0f, 1f)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(TRACK_HEIGHT)
    ) {
        val trackWidth = maxWidth
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(TRACK_HEIGHT)
                .background(color = theme.color.border.soft)
        )
        Box(
            modifier = Modifier
                .offset(x = trackWidth * startFraction)
                .width(trackWidth * (endFraction - startFraction).coerceAtLeast(0f))
                .height(TRACK_HEIGHT)
                .background(color = theme.color.surface.foregroundInvertedPrimary)
        )
    }
}

private val TRACK_HEIGHT = 2.dp
private val THUMB_SHADOW_ELEVATION = 2.dp
