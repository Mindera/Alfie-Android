package com.mindera.alfie.designsystem.component.datepicker

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.core.commons.util.DateUtils
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonSize
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.theme.Theme

private const val DATE_FORMAT = "dd/MM/yyyy"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(
    onSelect: ClickEventOneArg<String>,
    onDismiss: ClickEvent,
    state: DatePickerState = rememberDatePickerState()
) {
    val selectedDate = DateUtils.convertMsToDate(
        date = state.selectedDateMillis ?: 0L,
        dateFormat = DATE_FORMAT
    )
    DatePickerDialog(
        confirmButton = {
            Button(
                type = ButtonType.Primary,
                onClick = { onSelect(selectedDate) },
                text = stringResource(id = R.string.button_ok),
                buttonSize = ButtonSize.Medium,
                iconButton = null,
                isEnabled = true
            )
        },
        dismissButton = {
            Button(
                type = ButtonType.Primary,
                onClick = onDismiss,
                text = stringResource(id = R.string.button_cancel),
                buttonSize = ButtonSize.Medium,
                iconButton = null,
                isEnabled = true
            )
        },
        onDismissRequest = onDismiss
    ) {
        DatePicker(state = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun DateDialogPreview() {
    Theme {
        val datePickerState = rememberDatePickerState()
        DateDialog(
            onSelect = { },
            onDismiss = { },
            state = datePickerState
        )
    }
}
