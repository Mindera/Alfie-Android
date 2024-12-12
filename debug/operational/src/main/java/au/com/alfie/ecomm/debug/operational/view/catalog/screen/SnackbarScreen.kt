package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonSize
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomHostState
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomVisuals
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarPriority.HIGH
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarTimeDuration.INDEFINITE
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarTimeDuration.SHORT
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarType
import au.com.alfie.ecomm.designsystem.component.switch.Switch
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import au.com.alfie.ecomm.designsystem.R as RD

@Destination
@Composable
fun SnackbarScreen(
    topBarState: TopBarState,
    snackbarCustomHostState: SnackbarCustomHostState,
    navigator: DestinationsNavigator
) {
    val coroutineScope = rememberCoroutineScope()
    var visuals = SnackbarCustomVisuals(
        type = SnackbarType.Info,
        message = "This is a Snackbar",
        withDismissAction = false,
        priority = HIGH,
        timeDuration = SHORT
    )

    topBarState.logoTopBar(showNavigationIcon = true)

    BackHandler {
        snackbarCustomHostState.dismissSnackbar()
        navigator.navigateUp()
    }

    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing16)
            .scrollable(
                state = rememberScrollState(),
                orientation = Orientation.Vertical
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Theme.spacing.spacing12),
            textAlign = TextAlign.Start,
            text = "Options",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        SwitchRow(
            text = "Show Icon",
            startChecked = false
        ) {
            visuals = visuals.copy(
                icon = if (it) RD.drawable.ic_informational_checkmark else null
            )
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        SwitchRow(text = "Show Action Button", startChecked = false) {
            visuals = visuals.copy(
                actionLabel = if (it) "Undo" else null
            )
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        SwitchRow(text = "Show Close Button", startChecked = false) {
            visuals = visuals.copy(withDismissAction = it)
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        SwitchRow(text = "Auto dismiss", startChecked = true) {
            visuals = visuals.copy(
                timeDuration = if (it) SHORT else INDEFINITE
            )
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        SnackbarButton(
            text = "Show Single Line",
            coroutineScope = coroutineScope,
            snackbarCustomHostState = snackbarCustomHostState
        ) { visuals.copy(singleLine = true) }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        SnackbarButton(
            text = "Show Multi Line",
            coroutineScope = coroutineScope,
            snackbarCustomHostState = snackbarCustomHostState
        ) {
            visuals.copy(
                message = "This is a Snackbar with a long text to show how it works with multiple lines",
                singleLine = false
            )
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        SnackbarButton(
            text = "Show Success",
            coroutineScope = coroutineScope,
            snackbarCustomHostState = snackbarCustomHostState
        ) { visuals.copy(type = SnackbarType.Success) }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        SnackbarButton(
            text = "Show Error",
            coroutineScope = coroutineScope,
            snackbarCustomHostState = snackbarCustomHostState
        ) { visuals.copy(type = SnackbarType.Error) }
    }
}

@Composable
private fun SwitchRow(
    text: String,
    startChecked: Boolean,
    onCheckChange: ClickEventOneArg<Boolean>
) {
    var checked by remember { mutableStateOf(startChecked) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Theme.spacing.spacing12)
    ) {
        Text(
            text = text,
            style = Theme.typography.paragraph
        )
        Switch(
            checked = checked,
            onCheckChange = {
                checked = it
                onCheckChange(checked)
            }
        )
    }
}

@Composable
private fun SnackbarButton(
    text: String,
    coroutineScope: CoroutineScope,
    snackbarCustomHostState: SnackbarCustomHostState,
    getVisuals: () -> SnackbarCustomVisuals
) {
    Button(
        type = ButtonType.Primary,
        text = text,
        buttonSize = ButtonSize.Large,
        modifier = Modifier.width(200.dp),
        onClick = {
            coroutineScope.launch {
                snackbarCustomHostState.showSnackbar(getVisuals())
            }
        }
    )
}
