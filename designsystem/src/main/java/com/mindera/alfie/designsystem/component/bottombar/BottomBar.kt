package com.mindera.alfie.designsystem.component.bottombar

import androidx.annotation.DrawableRes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.event.ClickEventTwoArg
import com.mindera.alfie.core.ui.util.stringResource
import com.mindera.alfie.designsystem.animation.DefaultVisibilityAnimation
import com.mindera.alfie.designsystem.animation.standard
import com.mindera.alfie.designsystem.component.badge.BadgeType
import com.mindera.alfie.designsystem.component.badge.IconBadge
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BottomBar(
    state: BottomBarState,
    items: ImmutableList<BottomBarItem>,
    onItemClick: ClickEventTwoArg<Int, BottomBarItem>,
    modifier: Modifier = Modifier
) {
    val isVisible = state.isVisible

    DefaultVisibilityAnimation(
        isVisible = isVisible,
        enterTransition = fadeIn(animationSpec = standard()),
        exitTransition = fadeOut(animationSpec = standard())
    ) {
        BottomBarContainer(modifier = modifier) {
            items.forEachIndexed { index, item ->
                BottomBarItem(
                    state = item.state,
                    label = stringResource(resource = item.label),
                    icon = item.icon,
                    onClick = { if (isVisible) onItemClick(index, item) },
                    // D8: Figma items are `flex: 1 0 0` — equal share of the bar rather than a
                    // fixed 86 dp minimum, which would overflow a 360 dp screen at five tabs.
                    modifier = Modifier
                        .weight(1f)
                        .testTag(item.testTag)
                )
            }
        }
    }
}

@Composable
private fun BottomBarContainer(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val theme = LocalTheme.current
    val borderColor = theme.color.border.soft
    val borderWidth = theme.primitive.border.weightDefault

    // D5: Figma has no drop shadow — the bar is separated from content by its top border alone.
    Surface(
        color = theme.color.surface.backgroundPrimary,
        contentColor = theme.color.content.contentPrimary,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // D9: drawn as a rect rather than a centred `drawLine` at y=0, which clipped half
                // the stroke and rendered ~0.5 dp instead of the specified 1 dp.
                .drawBehind {
                    drawRect(
                        color = borderColor,
                        topLeft = Offset.Zero,
                        size = Size(size.width, borderWidth.toPx())
                    )
                }
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing8),
            verticalAlignment = Alignment.Top,
            content = content
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BottomBarItem(
    state: BottomBarItemState,
    label: String,
    @DrawableRes icon: Int,
    onClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    // D6: selection is conveyed by colour alone — no indicator, no filled-icon variant.
    val color = if (state.isSelected) {
        theme.color.content.contentPrimary
    } else {
        theme.color.content.contentTerciary
    }

    Column(
        modifier = modifier
            // D12: `onDoubleClick` is kept so a fast double-tap fires once immediately instead of
            // waiting out the double-tap timeout. Behaviour is unchanged from before the rollout.
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onClick,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            )
            // The bar's vertical padding lives here, inside `combinedClickable`, so the whole 76 dp
            // cell is tappable. On the container `Row` it would sit outside each tab's touch target.
            .padding(
                top = theme.spacing.spacing8,
                bottom = theme.spacing.spacing16
            )
            // `mergeDescendants` is stated explicitly rather than relied upon: `combinedClickable`
            // already merges, so TalkBack reads the whole cell as "Home, tab, selected" either way
            // (verified on device — the focus rect wraps icon + label). Spelling it out keeps the
            // single-stop announcement from depending on a `combinedClickable` implementation detail.
            .semantics(mergeDescendants = true) { selected = state.isSelected },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing4)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(theme.spacing.spacing32),
            contentAlignment = Alignment.Center
        ) {
            IconBadge(badge = state.badge) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(theme.sizing.icon.medium),
                    tint = color
                )
            }
        }
        // D7: `label/small-bold` (Roboto Medium 12/16) replaces `body.small` (same metrics, Normal).
        Text(
            text = label,
            style = theme.typography.label.smallBold,
            color = color,
            textAlign = TextAlign.Center,
            maxLines = 1,
            // Five weighted tabs leave ~65 dp each at 360 dp; the longer labels exceed that at large
            // font scales, where the default `Clip` would cut a glyph in half with no cue.
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun previewItem(
    label: String,
    @DrawableRes icon: Int,
    isSelected: Boolean = false,
    badge: BadgeType = BadgeType.None
) = object : BottomBarItem {
    override val state: BottomBarItemState =
        rememberBottomBarItemState(isSelected = isSelected, badge = badge)
    override val icon: Int = icon
    override val label: StringResource = StringResource.fromText(label)
    override val testTag: String = ""
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun BottomBarPreview() {
    Theme {
        BottomBar(
            state = rememberBottomBarState(),
            items = persistentListOf(
                previewItem(label = "Home", icon = AlfieIcons.Home, isSelected = true),
                previewItem(label = "Store", icon = AlfieIcons.Menu),
                previewItem(label = "Wishlist", icon = AlfieIcons.Wishlist),
                previewItem(label = "Bag", icon = AlfieIcons.Bag),
                previewItem(label = "Account", icon = AlfieIcons.Account)
            ),
            onItemClick = { _, _ -> }
        )
    }
}

/** Guards the label overflow: five weighted tabs at 360 dp are the tightest case for the long labels. */
@Preview(showBackground = true, widthDp = 360, fontScale = 1.5f)
@Composable
private fun BottomBarLargeFontPreview() {
    Theme {
        BottomBar(
            state = rememberBottomBarState(),
            items = persistentListOf(
                previewItem(label = "Home", icon = AlfieIcons.Home, isSelected = true),
                previewItem(label = "Store", icon = AlfieIcons.Menu),
                previewItem(label = "Wishlist", icon = AlfieIcons.Wishlist),
                previewItem(label = "Bag", icon = AlfieIcons.Bag),
                previewItem(label = "Account", icon = AlfieIcons.Account)
            ),
            onItemClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun BottomBarWithBadgePreview() {
    Theme {
        BottomBar(
            state = rememberBottomBarState(),
            items = persistentListOf(
                previewItem(label = "Home", icon = AlfieIcons.Home),
                previewItem(label = "Store", icon = AlfieIcons.Menu),
                previewItem(label = "Wishlist", icon = AlfieIcons.Wishlist),
                previewItem(
                    label = "Bag",
                    icon = AlfieIcons.Bag,
                    isSelected = true,
                    badge = BadgeType.Counter(count = 3)
                ),
                previewItem(label = "Account", icon = AlfieIcons.Account)
            ),
            onItemClick = { _, _ -> }
        )
    }
}
