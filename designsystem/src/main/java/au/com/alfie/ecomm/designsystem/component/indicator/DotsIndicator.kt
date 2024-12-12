package au.com.alfie.ecomm.designsystem.component.indicator

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.animation.standard
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun DotsIndicator(
    currentItem: () -> Int,
    itemCount: Int,
    size: DotsIndicatorSize,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            space = Theme.spacing.spacing8,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        repeat(itemCount) { iteration ->
            val isSelected by remember { derivedStateOf { currentItem() % itemCount == iteration } }
            val color by animateColorAsState(
                targetValue = if (isSelected) Theme.color.primary.mono700 else Theme.color.primary.mono200,
                animationSpec = standard(),
                label = "IndicatorDot${iteration}Color"
            )
            Box(
                modifier = Modifier
                    .size(size.size)
                    .drawBehind {
                        drawCircle(color)
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DotsIndicatorPreview() {
    Theme {
        Column {
            DotsIndicator(
                currentItem = { 0 },
                itemCount = 3,
                size = DotsIndicatorSize.Small
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            DotsIndicator(
                currentItem = { 1 },
                itemCount = 3,
                size = DotsIndicatorSize.Medium
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            DotsIndicator(
                currentItem = { 2 },
                itemCount = 3,
                size = DotsIndicatorSize.Large
            )
        }
    }
}
