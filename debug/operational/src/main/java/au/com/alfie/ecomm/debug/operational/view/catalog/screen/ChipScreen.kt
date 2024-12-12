package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.component.chip.Chip
import au.com.alfie.ecomm.designsystem.component.chip.ChipGroup
import au.com.alfie.ecomm.designsystem.component.chip.ChipProperties
import au.com.alfie.ecomm.designsystem.component.chip.ChipType
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ChipScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.padding(Theme.spacing.spacing16)
        ) {
            Text(
                text = "Chips",
                style = Theme.typography.heading3
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
            HorizontalDivider()
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = Theme.spacing.spacing16),
                verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing12)
            ) {
                Text(
                    text = "Regular",
                    style = Theme.typography.paragraphBold
                )
                Chip(
                    label = "Default",
                    counter = 12,
                    onClickEvent = {},
                    isSelected = false
                )
                Chip(
                    label = "Selected",
                    onClickEvent = {},
                    isSelected = true
                )
                Chip(
                    label = "Disabled",
                    onClickEvent = {},
                    isSelected = false,
                    isEnabled = false
                )
                Chip(
                    label = "Disabled Selected",
                    onClickEvent = {},
                    isSelected = true,
                    isEnabled = false
                )
            }
            Column(
                modifier = Modifier.padding(end = Theme.spacing.spacing16),
                verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing12)
            ) {
                Text(
                    text = "Large",
                    style = Theme.typography.paragraphBold
                )
                Chip(
                    label = "Default",
                    chipType = ChipType.LARGE,
                    counter = 12,
                    onClickEvent = {},
                    isSelected = false
                )
                Chip(
                    label = "Selected",
                    chipType = ChipType.LARGE,
                    onClickEvent = {},
                    isSelected = true
                )
                Chip(
                    label = "Disabled",
                    chipType = ChipType.LARGE,
                    onClickEvent = {},
                    isSelected = false,
                    isEnabled = false
                )
                Chip(
                    label = "Disabled Selected",
                    chipType = ChipType.LARGE,
                    onClickEvent = {},
                    isSelected = true,
                    isEnabled = false
                )
            }
        }

        Column {
            Text(
                modifier = Modifier.padding(
                    vertical = Theme.spacing.spacing12,
                    horizontal = Theme.spacing.spacing16
                ),
                text = "Single selection group",
                style = Theme.typography.heading3
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = Theme.spacing.spacing16))
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            val chipPropertiesSingle = remember {
                mutableStateListOf(
                    ChipProperties(
                        label = "Option A",
                        counter = 12,
                        isSelected = false,
                        isEnabled = true
                    ),
                    ChipProperties(
                        label = "Option B",
                        counter = 1234,
                        isSelected = false,
                        isEnabled = true
                    ),
                    ChipProperties(
                        label = "Option C",
                        counter = 0,
                        isSelected = false,
                        isEnabled = true
                    ),
                    ChipProperties(
                        label = "Option D",
                        isSelected = false,
                        isEnabled = true
                    ),
                    ChipProperties(
                        label = "Disabled",
                        isSelected = false,
                        isEnabled = false
                    )
                )
            }
            ChipGroup(
                chips = chipPropertiesSingle.toList(),
                onSelectionChange = { index ->
                    chipPropertiesSingle.replaceAll { it.copy(isSelected = false) }
                    chipPropertiesSingle[index] = chipPropertiesSingle[index].copy(
                        isSelected = chipPropertiesSingle[index].isSelected.not()
                    )
                }
            )
        }
        Column {
            Text(
                modifier = Modifier.padding(
                    vertical = Theme.spacing.spacing12,
                    horizontal = Theme.spacing.spacing16
                ),
                text = "Multiple selection group",
                style = Theme.typography.heading3
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = Theme.spacing.spacing16))
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            val chipPropertiesMulti = remember {
                mutableStateListOf(
                    ChipProperties(
                        label = "Option A",
                        counter = 12,
                        isSelected = false,
                        isEnabled = true,
                        isDismissible = true
                    ),
                    ChipProperties(
                        label = "Option B",
                        counter = 1234,
                        isSelected = false,
                        isEnabled = true,
                        isDismissible = true
                    ),
                    ChipProperties(
                        label = "Option C",
                        counter = 0,
                        isSelected = false,
                        isEnabled = true,
                        isDismissible = true
                    ),
                    ChipProperties(
                        label = "Option D",
                        isSelected = false,
                        isEnabled = true,
                        isDismissible = true
                    ),
                    ChipProperties(
                        label = "Disabled",
                        isSelected = false,
                        isEnabled = false,
                        isDismissible = true
                    )
                )
            }
            ChipGroup(
                chips = chipPropertiesMulti.toList(),
                onSelectionChange = { index ->
                    chipPropertiesMulti[index] = chipPropertiesMulti[index].copy(
                        isSelected = chipPropertiesMulti[index].isSelected.not()
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ChipScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Chip Screen"),
        showNavigationIcon = false
    )
    ChipScreen(topBarState = topBarState)
}
