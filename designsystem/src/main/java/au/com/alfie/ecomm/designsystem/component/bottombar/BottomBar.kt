package au.com.alfie.ecomm.designsystem.component.bottombar

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.event.ClickEventTwoArg
import au.com.alfie.ecomm.core.ui.util.stringResource
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.animation.DefaultVisibilityAnimation
import au.com.alfie.ecomm.designsystem.animation.standard
import au.com.alfie.ecomm.designsystem.component.badge.IconBadge
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private val BOTTOM_BAR_HEIGHT = 63.dp

@Composable
fun BottomBar(
    state: BottomBarState,
    items: ImmutableList<BottomBarItem>,
    onItemClick: ClickEventTwoArg<Int, BottomBarItem>,
    modifier: Modifier = Modifier
) {
    var selectedPosition by remember { mutableStateOf(Offset.Zero to Offset.Zero) }
    val isVisible = state.isVisible

    DefaultVisibilityAnimation(
        isVisible = isVisible,
        enterTransition = fadeIn(animationSpec = standard()),
        exitTransition = fadeOut(animationSpec = standard())
    ) {
        BottomBarContainer(
            selectedStartPosition = selectedPosition.first,
            selectedEndPosition = selectedPosition.second,
            modifier = modifier
        ) {
            items.forEachIndexed { index, item ->
                BottomBarItem(
                    state = item.state,
                    label = stringResource(resource = item.label),
                    icon = item.icon,
                    onClick = { if (isVisible) onItemClick(index, item) },
                    modifier = Modifier.testTag(item.testTag),
                    onItemPosition = { start, end ->
                        if (item.state.isSelected) {
                            selectedPosition = start to end
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomBarContainer(
    selectedStartPosition: Offset,
    selectedEndPosition: Offset,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = Theme.color.white,
        contentColor = Theme.color.primary.mono900,
        shadowElevation = Theme.elevation.elevation3,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.drawBehind {
                drawLine(
                    color = Theme.color.primary.mono200,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = BOTTOM_BAR_HEIGHT)
                    .selectableGroup(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
            BottomBarIndicator(
                startPosition = selectedStartPosition,
                endPosition = selectedEndPosition,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BottomBarItem(
    state: BottomBarItemState,
    label: String,
    @DrawableRes icon: Int,
    onClick: ClickEvent,
    onItemPosition: (start: Offset, end: Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onClick,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            )
            .defaultMinSize(minHeight = BOTTOM_BAR_HEIGHT)
            .widthIn(min = 86.dp)
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInWindow()
                onItemPosition(bounds.topLeft, bounds.bottomRight)
            },
        contentAlignment = Alignment.Center
    ) {
        val color = if (state.isSelected) Theme.color.primary.mono900 else Theme.color.primary.mono500
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconBadge(badge = state.badge) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(Theme.iconSize.medium),
                    tint = color
                )
            }
            Spacer(modifier = Modifier.height(Theme.spacing.spacing8))
            Text(
                text = label,
                style = Theme.typography.small,
                color = color
            )
        }
    }
}

@Composable
private fun BottomBarIndicator(
    startPosition: Offset,
    endPosition: Offset,
    modifier: Modifier = Modifier
) {
    val animation = tween<Offset>(
        durationMillis = 500,
        easing = EaseOutCubic
    )
    val startOffset by animateOffsetAsState(
        targetValue = startPosition,
        animationSpec = animation,
        label = "StartOffsetAnimation"
    )
    val endOffset by animateOffsetAsState(
        targetValue = endPosition,
        animationSpec = animation,
        label = "EndOffsetAnimation"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
    ) {
        drawIndicator(
            color = Theme.color.primary.mono900,
            startOffset = startOffset,
            endOffset = endOffset
        )
    }
}

private fun DrawScope.drawIndicator(
    color: Color,
    startOffset: Offset,
    endOffset: Offset
) {
    val cornerRadius = CornerRadius(size.height, size.height)
    val path = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(startOffset.x, 0f),
                    size = Size(endOffset.x - startOffset.x, size.height)
                ),
                bottomLeft = cornerRadius,
                bottomRight = cornerRadius
            )
        )
    }
    drawPath(
        path = path,
        color = color
    )
}

@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    Theme {
        var selectedItem by remember { mutableIntStateOf(0) }

        BottomBar(
            state = rememberBottomBarState(),
            items = persistentListOf(
                object : BottomBarItem {
                    override val state: BottomBarItemState = rememberBottomBarItemState()
                    override val icon: Int = R.drawable.ic_action_house
                    override val label: StringResource = StringResource.fromText("Home")
                    override val testTag: String = ""
                },
                object : BottomBarItem {
                    override val state: BottomBarItemState = rememberBottomBarItemState()
                    override val icon: Int = R.drawable.ic_informational_store
                    override val label: StringResource = StringResource.fromText("Shop")
                    override val testTag: String = ""
                },
                object : BottomBarItem {
                    override val state: BottomBarItemState = rememberBottomBarItemState()
                    override val icon: Int = R.drawable.ic_action_heart_outline
                    override val label: StringResource = StringResource.fromText("Wishlist")
                    override val testTag: String = ""
                },
                object : BottomBarItem {
                    override val state: BottomBarItemState = rememberBottomBarItemState()
                    override val icon: Int = R.drawable.ic_action_bag
                    override val label: StringResource = StringResource.fromText("Bag")
                    override val testTag: String = ""
                }
            ),
            onItemClick = { index, _ ->
                selectedItem = index
            }
        )
    }
}
