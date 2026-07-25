package com.mindera.alfie.designsystem.component.loading

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

// Figma "Loading Spinner" (Design System, node 6619-47283): a thin rounded indeterminate arc.
// The Small (24dp) reference has a ~2dp stroke (1dp inset each side), i.e. diameter / 12; the stroke
// scales proportionally with the diameter across sizes.
private const val STROKE_TO_DIAMETER_RATIO = 12f

/**
 * Design-system loading spinner — an indeterminate, rotating circular arc.
 *
 * Sizes follow the Figma spec (node 6619-47283): [LoadingSpinnerSize.Small] 24dp,
 * [LoadingSpinnerSize.Medium] 32dp, [LoadingSpinnerSize.Large] 48dp. The arc colour defaults to
 * `content/content-primary`; pass [color] to override (e.g. on a dark surface).
 */
@Composable
fun LoadingSpinner(
    modifier: Modifier = Modifier,
    size: LoadingSpinnerSize = LoadingSpinnerSize.Small,
    color: Color = LocalTheme.current.color.content.contentPrimary
) {
    val diameter = size.dimension()
    CircularProgressIndicator(
        modifier = modifier.size(diameter),
        color = color,
        strokeWidth = diameter / STROKE_TO_DIAMETER_RATIO,
        trackColor = Color.Transparent,
        strokeCap = StrokeCap.Round
    )
}

enum class LoadingSpinnerSize {
    Small,
    Medium,
    Large
}

@Composable
private fun LoadingSpinnerSize.dimension(): Dp {
    val spacing = LocalTheme.current.spacing
    return when (this) {
        LoadingSpinnerSize.Small -> spacing.spacing24 // 24dp
        LoadingSpinnerSize.Medium -> spacing.spacing32 // 32dp
        LoadingSpinnerSize.Large -> spacing.spacing48 // 48dp
    }
}

@Preview
@Composable
private fun LoadingSpinnerPreview() {
    Theme {
        LoadingSpinner(size = LoadingSpinnerSize.Small)
    }
}

@Preview
@Composable
private fun LoadingSpinnerMediumPreview() {
    Theme {
        LoadingSpinner(size = LoadingSpinnerSize.Medium)
    }
}

@Preview
@Composable
private fun LoadingSpinnerLargePreview() {
    Theme {
        LoadingSpinner(size = LoadingSpinnerSize.Large)
    }
}
