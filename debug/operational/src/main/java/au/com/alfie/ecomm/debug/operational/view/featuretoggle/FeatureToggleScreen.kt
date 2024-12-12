package au.com.alfie.ecomm.debug.operational.view.featuretoggle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.switch.Switch
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggle
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggleType
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
internal fun FeatureToggleScreen(
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)
    bottomBarState.hideBottomBar()
    val viewModel: FeatureToggleViewModel = hiltViewModel()
    val featureToggles = viewModel.featureToggle.collectAsStateWithLifecycle()

    LazyColumn {
        items(featureToggles.value.size) { index ->
            val item = featureToggles.value[index]
            if (item.type ==
                FeatureToggleType.SWITCH.value
            ) {
                SwitchItem(
                    text = item.toggleTitle,
                    isChecked = item.enabled,
                    onCheckChange = {
                        viewModel.updateFeatureToggle(
                            FeatureToggle(
                                item.toggleTitle,
                                it,
                                item.type
                            )
                        )
                    }
                )
            }
            Divider(
                color = Color.LightGray,
                modifier = Modifier.padding(
                    start = Theme.spacing.spacing6,
                    end = Theme.spacing.spacing6
                )
            )
        }
    }
}

@Composable
internal fun SwitchItem(
    text: String,
    isChecked: Boolean,
    onCheckChange: ClickEventOneArg<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Theme.spacing.spacing4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing16),
            text = text,
            style = Theme.typography.paragraph
        )
        Switch(
            modifier = Modifier.padding(Theme.spacing.spacing16),
            checked = isChecked,
            onCheckChange = onCheckChange,
            enabled = true
        )
    }
}
