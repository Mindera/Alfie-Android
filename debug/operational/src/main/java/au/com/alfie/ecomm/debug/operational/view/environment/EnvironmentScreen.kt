package au.com.alfie.ecomm.debug.operational.view.environment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import au.com.alfie.ecomm.core.commons.string.toString
import au.com.alfie.ecomm.debug.operational.R
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentEvent
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentEvent.OnEnvironmentSelected
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentEvent.OnSaveClick
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentEvent.OnUrlChange
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentState
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentUI
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentUIEvent.Restart
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentUIEvent.ShowErrorSnackbar
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentUIEvent.ShowSuccessSnackbar
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.component.input.TextField
import au.com.alfie.ecomm.designsystem.component.input.TextFieldType.Default
import au.com.alfie.ecomm.designsystem.component.radio.RadioButtonGroup
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomHostState
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarType
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
internal fun EnvironmentScreen(
    topBarState: TopBarState,
    bottomBarState: BottomBarState,
    snackbarCustomHostState: SnackbarCustomHostState
) {
    topBarState.textTopBar(stringResource(id = R.string.environment_screen_title))
    bottomBarState.hideBottomBar()

    val viewModel: EnvironmentViewModel = hiltViewModel()
    val environmentState by viewModel.state.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is Restart -> viewModel.restart(context)
                is ShowSuccessSnackbar -> snackbarCustomHostState.showSnackbar(
                    type = SnackbarType.Success,
                    message = context.getString(R.string.environment_success_message),
                    withDismissAction = false
                )
                is ShowErrorSnackbar -> snackbarCustomHostState.showSnackbar(
                    type = SnackbarType.Error,
                    message = context.getString(R.string.environment_error_message)
                )
            }
        }
    }

    when (val state = environmentState) {
        is EnvironmentState.Data -> EnvironmentContent(
            environments = state.environments,
            onEvent = viewModel::handleEvent
        )
        EnvironmentState.Empty -> Unit
    }
}

@Composable
private fun EnvironmentContent(
    environments: List<EnvironmentUI>,
    onEvent: (EnvironmentEvent) -> Unit
) {
    val context = LocalContext.current
    var envSelected by remember { mutableIntStateOf(environments.indexOfFirst { it.isSelected }) }
    var urlSelected by remember { mutableStateOf(environments.getOrNull(envSelected)?.environment?.graphQLUrl.orEmpty()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
        Text(
            text = stringResource(id = R.string.environment_choose_label),
            style = Theme.typography.small
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
        RadioButtonGroup(
            options = environments.map { it.label.toString(context) },
            optionSelected = envSelected,
            onSelectionChange = {
                envSelected = it
                urlSelected = environments[it].environment.graphQLUrl
                onEvent(OnEnvironmentSelected(environments[it]))
            },
            isEnabled = { environments[it].isEnabled },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
        TextField(
            value = urlSelected,
            placeholder = "",
            type = Default,
            isEnabled = environments.getOrNull(envSelected)?.enableUrlChange ?: false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.spacing.spacing16),
            onTextChange = {
                urlSelected = it
                onEvent(OnUrlChange(it))
            }
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing24))
        Button(
            type = ButtonType.Primary,
            text = stringResource(id = R.string.environment_save_button),
            onClick = {
                keyboardController?.hide()
                onEvent(OnSaveClick)
            }
        )
    }
}
