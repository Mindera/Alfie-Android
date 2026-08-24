package com.mindera.alfie.designsystem.component.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

// Pagination (Design System, screens file PDP gallery): 6×6 dots in
// button/primary/background-primary-disabled, the selected page a wider 12×6 pill in
// button/primary/content-primary-default with a 1px stroke of the same colour, 8px apart.
private const val PILL_RADIUS = 50

/**
 * Gallery pagination dots over imagery: unselected dots are 6dp circles, the selected page renders
 * as a wider pill. Decorative by design — pair with a pager that already exposes the position to
 * accessibility.
 */
@Composable
fun PageIndicator(
    currentItem: Int,
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    if (itemCount <= 1) return

    val c = LocalTheme.current.color.button
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            space = LocalTheme.current.spacing.spacing8,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        repeat(itemCount) { index ->
            if (index == currentItem) {
                Dot(
                    // 6dp has no PrimitiveSpacing token — literal per the border-width carve-out
                    width = LocalTheme.current.spacing.spacing12,
                    height = 6.dp,
                    shape = RoundedCornerShape(percent = PILL_RADIUS),
                    color = c.primaryContentPrimaryDefault,
                    borderColor = c.primaryContentPrimaryDefault
                )
            } else {
                Dot(
                    width = 6.dp,
                    height = 6.dp,
                    shape = CircleShape,
                    color = c.primaryBackgroundPrimaryDisabled,
                    borderColor = null
                )
            }
        }
    }
}

@Composable
private fun Dot(
    width: Dp,
    height: Dp,
    shape: Shape,
    color: Color,
    borderColor: Color?
) {
    val borderWeight = LocalTheme.current.primitive.border.weightDefault
    val modifier = Modifier
        .size(width = width, height = height)
        .background(color = color, shape = shape)
    Box(
        modifier = if (borderColor != null) {
            modifier.border(
                width = borderWeight,
                color = borderColor,
                shape = shape
            )
        } else {
            modifier
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF111111)
@Composable
private fun PageIndicatorPreview() {
    Theme {
        PageIndicator(
            currentItem = 1,
            itemCount = 5
        )
    }
}
