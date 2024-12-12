package au.com.alfie.ecomm.designsystem.component.segmented

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.test.SEGMENTED_CONTROL
import au.com.alfie.ecomm.core.ui.test.SEGMENTED_OPTION
import au.com.alfie.ecomm.core.ui.util.stringResource
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val MIN_SEGMENTS = 2
private const val MAX_SEGMENTS = 3

private val indicatorAnimation = tween<Offset>(
    durationMillis = 500,
    easing = EaseOutCubic
)

@Composable
fun SegmentedControl(
    segments: ImmutableList<SegmentedItem>,
    selectedSegment: Int,
    onSegmentClick: ClickEventOneArg<Int>,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    require(segments.size in MIN_SEGMENTS..MAX_SEGMENTS)

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(40.dp)
            .clip(Theme.shape.small)
            .background(color = Theme.color.primary.mono100)
            .selectableGroup()
            .testTag(SEGMENTED_CONTROL)
    ) {
        val halfSegment = (constraints.maxWidth / segments.size) / 2f
        val selectedFraction = (selectedSegment + 1) / segments.size.toFloat()
        val offsetX = constraints.maxWidth * selectedFraction - halfSegment
        val offsetY = constraints.minHeight / 2f
        val initialIndicatorOffset = Offset(offsetX, offsetY)
        var selectedItemPosition by remember { mutableStateOf(initialIndicatorOffset to initialIndicatorOffset) }

        val indicatorTopLeft by animateOffsetAsState(
            targetValue = selectedItemPosition.first,
            animationSpec = indicatorAnimation,
            label = "IndicatorTopLeftAnimation"
        )
        val indicatorBottomRight by animateOffsetAsState(
            targetValue = selectedItemPosition.second,
            animationSpec = indicatorAnimation,
            label = "IndicatorBottomRightAnimation"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isCompact) Theme.spacing.spacing4 else Theme.spacing.spacing8)
                .drawBehind {
                    drawIndicator(
                        topLeft = indicatorTopLeft,
                        bottomRight = indicatorBottomRight
                    )
                }
        ) {
            segments.forEachIndexed { index, segment ->
                val isSelected = selectedSegment == index
                SegmentedControlItem(
                    segment = segment,
                    onClick = { onSegmentClick(index) },
                    isSelected = isSelected,
                    isCompact = isCompact,
                    modifier = Modifier.weight(1f),
                    onItemPosition = { topLeft, bottomRight ->
                        if (isSelected) {
                            selectedItemPosition = topLeft to bottomRight
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SegmentedControlItem(
    segment: SegmentedItem,
    onClick: ClickEvent,
    isSelected: Boolean,
    isCompact: Boolean,
    onItemPosition: (topLeft: Offset, bottomRight: Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Theme.color.black else Theme.color.primary.mono500,
        label = "SegmentedControlContentColorAnimation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(Theme.shape.extraSmall)
            .selectable(
                selected = isSelected,
                role = Role.Tab,
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInParent()
                onItemPosition(bounds.topLeft, bounds.bottomRight)
            }
            .testTag(SEGMENTED_OPTION)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = Theme.spacing.spacing12,
                vertical = if (isCompact) Theme.spacing.spacing8 else Theme.spacing.spacing16
            )
        ) {
            segment.icon?.let { icon ->
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(Theme.iconSize.small),
                    tint = contentColor
                )
                Spacer(modifier = Modifier.width(if (isCompact) Theme.spacing.spacing4 else Theme.spacing.spacing8))
            }
            Text(
                text = stringResource(resource = segment.label),
                style = Theme.typography.paragraph,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = contentColor
            )
        }
    }
}

private fun DrawScope.drawIndicator(
    topLeft: Offset,
    bottomRight: Offset
) {
    drawRoundRect(
        color = Theme.color.white,
        topLeft = topLeft,
        size = Size(
            width = bottomRight.x - topLeft.x,
            height = bottomRight.y - topLeft.y
        ),
        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
    )
}

@Preview
@Composable
private fun SegmentedControlPreview() {
    Theme {
        var selected by remember { mutableIntStateOf(0) }

        SegmentedControl(
            segments = persistentListOf(
                SegmentedItem(StringResource.fromText("Categories")),
                SegmentedItem(StringResource.fromText("Brands"))
            ),
            selectedSegment = selected,
            onSegmentClick = { selected = it }
        )
    }
}
