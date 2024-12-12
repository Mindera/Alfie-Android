package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.component.datepicker.DateDialog
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import au.com.alfie.ecomm.designsystem.R as RD

@Destination
@Composable
fun DateFieldScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Theme.spacing.spacing8)
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Date Picker",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        DateField(isFutureDateEnabled = true)
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Date Picker with constraints",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        DateField(isFutureDateEnabled = false)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateField(isFutureDateEnabled: Boolean = true) {
    val datePlaceholder = stringResource(id = RD.string.date_input_placeholder)
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean =
            if (isFutureDateEnabled) true else utcTimeMillis <= System.currentTimeMillis()
    })

    var isDialogOpen by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(datePlaceholder) }

    Box(
        modifier = Modifier.clickable { isDialogOpen = true }
    ) {
        // TODO: update after input fields are available
        TextField(
            value = selected,
            enabled = false,
            onValueChange = { },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = ""
                )
            }
        )
    }

    if (isDialogOpen) {
        DateDialog(
            onSelect = {
                selected = it
                isDialogOpen = false
            },
            onDismiss = {
                isDialogOpen = false
            },
            state = datePickerState
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DateFieldScreenPreview() {
    Theme {
        val topBarState = TopBarState(
            title = TopBarTitle.Text("Date Screen"),
            showNavigationIcon = false
        )
        DateFieldScreen(topBarState = topBarState)
    }
}
