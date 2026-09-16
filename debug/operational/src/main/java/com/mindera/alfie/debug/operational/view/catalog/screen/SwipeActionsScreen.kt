package com.mindera.alfie.debug.operational.view.catalog.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.debug.operational.view.catalog.util.HeaderDivider
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.component.swipe.SwipeAction
import com.mindera.alfie.designsystem.component.swipe.SwipeActionType
import com.mindera.alfie.designsystem.component.swipe.SwipeActions
import com.mindera.alfie.designsystem.component.swipe.rememberSwipeActionsState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf

@Destination
@Composable
fun SwipeActionsScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)
    val swipeState = rememberSwipeActionsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16),
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = Theme.spacing.spacing24)
    ) {
        HeaderDivider(text = "Swipe left to reveal")
        SwipeActions(actions = bagActions()) {
            SwipeableRow(label = "Drag me left")
        }

        HeaderDivider(text = "Opened by a button")
        SwipeActions(
            actions = bagActions(),
            state = swipeState
        ) {
            SwipeableRow(label = "Or use the button below")
        }
        Button(
            type = ButtonType.Secondary,
            text = "Toggle actions",
            onClick = swipeState::toggle,
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
        )

        HeaderDivider(text = "Single destructive action")
        SwipeActions(
            actions = persistentListOf(
                SwipeAction(
                    icon = AlfieIcons.Delete,
                    label = "Delete",
                    onClick = {},
                    type = SwipeActionType.Destructive
                )
            )
        ) {
            SwipeableRow(label = "One panel only")
        }
    }
}

private fun bagActions() = persistentListOf(
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

@Composable
private fun SwipeableRow(label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalTheme.current.color.surface.backgroundPrimary)
            .padding(Theme.spacing.spacing32)
    ) {
        Text(
            text = label,
            style = LocalTheme.current.typography.body.medium,
            color = LocalTheme.current.color.content.contentPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwipeActionsScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Swipe Actions"),
        showNavigationIcon = false
    )
    Theme {
        SwipeActionsScreen(topBarState = topBarState)
    }
}
