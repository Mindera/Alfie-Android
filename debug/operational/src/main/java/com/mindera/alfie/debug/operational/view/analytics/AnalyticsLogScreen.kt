package com.mindera.alfie.debug.operational.view.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.debug.operational.R
import com.mindera.alfie.debug.operational.analytics.data.AnalyticsLogData
import com.mindera.alfie.debug.operational.view.analytics.model.AnalyticsLogState
import com.mindera.alfie.debug.operational.view.analytics.model.AnalyticsLogUI
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
internal fun AnalyticsLogScreen(
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    topBarState.textTopBar(stringResource(id = R.string.debug_screen_analytics_logs_label))
    bottomBarState.hideBottomBar()

    val viewModel = hiltViewModel<AnalyticsLogViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AnalyticsLogContent(
        state = state,
        onFilterSelect = viewModel::filterByTracker
    )
}

@Composable
private fun AnalyticsLogContent(
    state: AnalyticsLogState,
    onFilterSelect: ClickEventOneArg<String>
) {
    when (state) {
        is AnalyticsLogState.Data -> AnalyticsLogData(
            data = state.analyticsLogUI,
            onFilterSelect = onFilterSelect
        )
        AnalyticsLogState.Empty -> AnalyticsLogEmpty()
    }
}

@Composable
private fun AnalyticsLogEmpty() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Theme.spacing.spacing16),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.analytics_log_screen_empty))
    }
}

@Composable
private fun AnalyticsLogData(
    data: AnalyticsLogUI,
    onFilterSelect: ClickEventOneArg<String>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
    ) {
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Dropdown(
            keys = data.trackers,
            onFilterSelect = { key ->
                onFilterSelect(key)
            }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(data.events) {
                Event(analyticsLogData = it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Dropdown(
    keys: Set<String>,
    onFilterSelect: ClickEventOneArg<String>
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    var dropdownValue by remember { mutableStateOf(keys.firstOrNull().orEmpty()) }

    val c = LocalTheme.current.primitive.colors
    ExposedDropdownMenuBox(
        expanded = dropdownExpanded,
        onExpandedChange = { dropdownExpanded = !dropdownExpanded }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            value = dropdownValue,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = c.neutrals100,
                focusedContainerColor = c.neutrals100
            ),
            label = {
                Text(
                    text = stringResource(id = R.string.analytics_log_screen_trackers_label),
                    style = Theme.typography.paragraph
                )
            }
        )
        ExposedDropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
        ) {
            keys.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = it,
                            style = Theme.typography.small
                        )
                    },
                    onClick = {
                        dropdownValue = it
                        dropdownExpanded = false
                        onFilterSelect(it)
                    }
                )
            }
        }
    }
}

@Composable
private fun Event(analyticsLogData: AnalyticsLogData) {
    val c = LocalTheme.current.primitive.colors
    Column {
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            text = analyticsLogData.tracker,
            style = Theme.typography.tiny,
            color = c.neutrals400
        )
        Text(
            text = analyticsLogData.timestamp,
            style = Theme.typography.tinyItalic,
            color = c.neutrals500
        )
        Text(
            text = analyticsLogData.event,
            style = Theme.typography.paragraphBold
        )
        analyticsLogData.params.forEach { (key, value) ->
            Text(
                text = "$key: $value",
                style = Theme.typography.paragraph,
                color = c.neutrals500
            )
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing8))
        HorizontalDivider()
    }
}
