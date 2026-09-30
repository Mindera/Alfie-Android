package com.mindera.alfie.designsystem.component.swipe

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.roundToInt

/**
 * Horizontal swipe-to-reveal row.
 *
 * [content] slides left to uncover [actions], which are laid out end-aligned at the row's full
 * height. The row settles Open or Closed and stays there, and closes itself once an action fires.
 * Pass [state] to also open it from a button.
 *
 * Distinct from [SwipeAnchored], which drags vertically with a partial anchor for `BottomCard`.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeActions(
    actions: ImmutableList<SwipeAction>,
    modifier: Modifier = Modifier,
    state: SwipeActionsState = rememberSwipeActionsState(),
    content: @Composable () -> Unit
) {
    if (actions.isEmpty()) {
        Box(modifier = modifier) { content() }
        return
    }

    var revealWidthPx by remember { mutableIntStateOf(0) }
    // While the row is shut the panels sit behind opaque content, but they would still be focusable
    // and activatable by TalkBack and switch access. Gate them on the row actually being revealed —
    // targetValue covers the drag that is still settling open.
    val isRevealed by remember(state) { derivedStateOf { state.isOpen } }

    Box(modifier = modifier) {
        // matchParentSize keeps the backdrop out of the Box's own sizing, so the row is as tall as
        // the content and fillMaxHeight below stays bounded inside a lazy list item.
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .matchParentSize()
                .then(if (isRevealed) Modifier else Modifier.clearAndSetSemantics { })
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalTheme.current.spacing.spacing4),
                modifier = Modifier
                    .fillMaxHeight()
                    .onSizeChanged { size ->
                        if (size.width == revealWidthPx) return@onSizeChanged
                        revealWidthPx = size.width
                        state.draggableState.updateAnchors(
                            DraggableAnchors {
                                SwipeActionsAnchor.Closed at 0f
                                SwipeActionsAnchor.Open at -size.width.toFloat()
                            }
                        )
                    }
            ) {
                actions.forEach { action ->
                    SwipeActionPanel(
                        action = action,
                        isEnabled = isRevealed,
                        onClick = {
                            action.onClick()
                            state.close()
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .offset {
                    val offset = state.draggableState.offset
                    IntOffset(
                        x = if (offset.isNaN()) 0 else offset.roundToInt(),
                        y = 0
                    )
                }
                .anchoredDraggable(
                    state = state.draggableState,
                    orientation = Orientation.Horizontal,
                    enabled = revealWidthPx > 0
                )
        ) {
            content()
        }
    }
}

@Composable
private fun SwipeActionPanel(
    action: SwipeAction,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val theme = LocalTheme.current
    val contentColor = action.type.contentColor()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = theme.spacing.spacing8,
            alignment = Alignment.CenterVertically
        ),
        modifier = Modifier
            // Panel width is fixed so every action reads the same regardless of label length.
            .width(100.dp)
            .fillMaxHeight()
            .background(action.type.backgroundColor())
            .clickable(enabled = isEnabled, role = Role.Button, onClick = onClick)
            .padding(
                horizontal = theme.spacing.spacing16,
                vertical = theme.spacing.spacing8
            )
            .semantics { contentDescription = action.contentDescription ?: action.label }
            .testTag(action.testTag)
    ) {
        Icon(
            painter = painterResource(id = action.icon),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(theme.sizing.icon.medium)
        )
        Text(
            text = action.label,
            style = theme.typography.body.medium,
            color = contentColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360)
@Composable
private fun SwipeActionsPreview() {
    Theme {
        SwipeActions(
            actions = persistentListOf(
                SwipeAction(
                    icon = AlfieIcons.Wishlist,
                    label = "Save",
                    onClick = {}
                ),
                SwipeAction(
                    icon = AlfieIcons.Close,
                    label = "Remove",
                    onClick = {},
                    type = SwipeActionType.Destructive
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .background(LocalTheme.current.color.surface.foregroundPrimary)
                    .padding(Theme.spacing.spacing32)
            ) {
                Text(text = "Swipe me left", style = LocalTheme.current.typography.body.medium)
            }
        }
    }
}
