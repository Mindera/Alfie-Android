package com.mindera.alfie.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonSize
import com.mindera.alfie.designsystem.component.button.ButtonSize.Large
import com.mindera.alfie.designsystem.component.button.ButtonSize.Medium
import com.mindera.alfie.designsystem.component.button.ButtonSize.Small
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.component.button.ButtonType.Primary
import com.mindera.alfie.designsystem.component.button.ButtonType.Secondary
import com.mindera.alfie.designsystem.component.button.ButtonType.Tertiary
import com.mindera.alfie.designsystem.component.button.ButtonType.Underlined
import com.mindera.alfie.designsystem.component.button.IconButton
import com.mindera.alfie.designsystem.component.button.IconPosition.Left
import com.mindera.alfie.designsystem.component.button.IconPosition.Right
import com.mindera.alfie.designsystem.component.radio.RadioButtonGroup
import com.mindera.alfie.designsystem.component.switch.Switch
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.mindera.alfie.designsystem.R as RD
import com.mindera.alfie.designsystem.icons.AlfieIcons

private const val COLUMN_COUNT = 2

@Destination
@Composable
fun ButtonScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    var buttonSizeOptionSelected by remember { mutableIntStateOf(0) }
    var isEnabled by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.spacing16),
        columns = GridCells.Fixed(COLUMN_COUNT)
    ) {
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Text(
                modifier = Modifier.padding(vertical = Theme.spacing.spacing12),
                text = "Properties",
                style = Theme.typography.heading3
            )
        }
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            HorizontalDivider()
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        }
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Row {
                Box(modifier = Modifier.weight(1F)) {
                    RadioButtonGroup(
                        options = listOf("Small", "Medium", "Large"),
                        optionSelected = buttonSizeOptionSelected,
                        onSelectionChange = { buttonSizeOptionSelected = it },
                        horizontalPadding = Theme.spacing.spacing12
                    )
                }

                Column(modifier = Modifier.weight(1F)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = isEnabled,
                            onCheckChange = { isEnabled = it }
                        )
                        Text(
                            modifier = Modifier.padding(Theme.spacing.spacing12),
                            text = "Enabled",
                            style = Theme.typography.paragraph
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = isLoading,
                            onCheckChange = { isLoading = it }
                        )
                        Text(
                            modifier = Modifier.padding(Theme.spacing.spacing12),
                            text = "Loading",
                            style = Theme.typography.paragraph
                        )
                    }
                }
            }
        }

        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Text(
                modifier = Modifier.padding(vertical = Theme.spacing.spacing12),
                text = "Buttons",
                style = Theme.typography.heading3
            )
        }

        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            HorizontalDivider()
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        }

        val buttonSize = when (buttonSizeOptionSelected) {
            0 -> Small
            1 -> Medium
            else -> Large
        }

        item {
            ShowButtonOptions(
                type = Primary,
                buttonSize = buttonSize,
                isEnabled = isEnabled,
                isLoading = isLoading
            )
        }

        item {
            ShowButtonOptions(
                type = Secondary,
                buttonSize = buttonSize,
                isEnabled = isEnabled,
                isLoading = isLoading
            )
        }

        item {
            ShowButtonOptions(
                type = Tertiary,
                buttonSize = buttonSize,
                isEnabled = isEnabled,
                isLoading = isLoading
            )
        }

        item {
            ShowButtonOptions(
                type = Underlined,
                buttonSize = buttonSize,
                isEnabled = isEnabled,
                isLoading = isLoading
            )
        }
    }
}

@Composable
private fun ShowButtonOptions(
    type: ButtonType,
    buttonSize: ButtonSize,
    isEnabled: Boolean,
    isLoading: Boolean
) {
    Column {
        Spacer(modifier = Modifier.height(Theme.spacing.spacing8))
        Text(
            text = type.name,
            style = Theme.typography.paragraph
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing8))
        Button(
            modifier = Modifier.wrapContentWidth(),
            type = type,
            onClick = { },
            text = "No Icon",
            buttonSize = buttonSize,
            iconButton = null,
            isEnabled = isEnabled,
            isLoading = isLoading
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing8))
        if (type != Underlined) {
            Button(
                modifier = Modifier.wrapContentWidth(),
                type = type,
                onClick = { },
                text = "Icon Left",
                buttonSize = buttonSize,
                iconButton = IconButton(iconResource = AlfieIcons.Back, position = Left),
                isEnabled = isEnabled,
                isLoading = isLoading
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing8))
            Button(
                modifier = Modifier.wrapContentWidth(),
                type = type,
                onClick = { },
                text = "Icon Right",
                buttonSize = buttonSize,
                iconButton = IconButton(iconResource = AlfieIcons.Forward, position = Right),
                isEnabled = isEnabled,
                isLoading = isLoading
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing8))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ButtonScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Button Screen"),
        showNavigationIcon = false
    )
    ButtonScreen(topBarState = topBarState)
}
