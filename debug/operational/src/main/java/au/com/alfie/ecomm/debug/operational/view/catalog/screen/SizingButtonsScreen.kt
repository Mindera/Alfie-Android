package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.component.divider.DividerType
import au.com.alfie.ecomm.designsystem.component.divider.HorizontalDivider
import au.com.alfie.ecomm.designsystem.component.sizingbutton.INVALID_INDEX
import au.com.alfie.ecomm.designsystem.component.sizingbutton.SizingButtonGroup
import au.com.alfie.ecomm.designsystem.component.sizingbutton.SizingButtonProperties
import au.com.alfie.ecomm.designsystem.component.sizingbutton.SizingButtonState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf

private val sizingOptions = persistentListOf(
    SizingButtonProperties(
        text = "Size A",
        state = SizingButtonState.Selectable
    ),
    SizingButtonProperties(
        text = "Size B",
        state = SizingButtonState.Selectable
    ),
    SizingButtonProperties(
        text = "Size C",
        state = SizingButtonState.Selectable
    ),
    SizingButtonProperties(
        text = "Size D",
        state = SizingButtonState.Selectable
    ),
    SizingButtonProperties(
        text = "Size E",
        state = SizingButtonState.Selectable
    ),
    SizingButtonProperties(
        text = "Size F",
        state = SizingButtonState.Selectable
    )
)

@Destination
@Composable
fun SizingButtonsScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.spacing8)
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Sizing",
            style = Theme.typography.heading3
        )
        HorizontalDivider(dividerType = DividerType.Solid1Mono100)
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Column {
            var selectedIndex: Int by rememberSaveable { mutableIntStateOf(INVALID_INDEX) }
            SizingButtonGroup(
                options = sizingOptions,
                selectedIndex = selectedIndex,
                onSelectedOption = { index -> selectedIndex = index },
                modifier = Modifier.padding(end = Theme.spacing.spacing16)
            )

            Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
            HorizontalDivider(dividerType = DividerType.Solid1Mono100)
            Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
            val options2 = persistentListOf(
                SizingButtonProperties(
                    text = "Size X",
                    state = SizingButtonState.Selectable
                ),
                SizingButtonProperties(
                    text = "Out of Stock",
                    state = SizingButtonState.OutOfStock
                ),
                SizingButtonProperties(
                    text = "Size Y",
                    state = SizingButtonState.Selectable
                )
            )
            var selectedIndex2: Int by rememberSaveable { mutableIntStateOf(INVALID_INDEX) }
            SizingButtonGroup(
                selectedIndex = selectedIndex2,
                onSelectedOption = { index -> selectedIndex2 = index },
                options = options2,
                modifier = Modifier.padding(end = Theme.spacing.spacing16)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SizingButtonsScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Sizing Buttons Screen"),
        showNavigationIcon = false
    )
    Theme {
        SizingButtonsScreen(topBarState = topBarState)
    }
}
