package com.mindera.alfie.feature.scanner.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.input.TextField
import com.mindera.alfie.designsystem.component.input.TextFieldType
import com.mindera.alfie.designsystem.component.modal.BottomSheet
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.scanner.R

/**
 * Manual barcode entry — Figma "Barcode manually" (node `3353:35100`).
 *
 * The design's sheet is a close button, a centred title and a single bordered input, which is
 * exactly what the design-system [BottomSheet] already renders, so it is reused rather than
 * rebuilt. Its `isFullscreen = false` variant is the half-height sheet the design shows.
 *
 * The drawn iOS keyboard is mockup furniture; its "Search" key is expressed here as
 * [ImeAction.Search], which Android's own IME renders.
 */
@Composable
internal fun ManualBarcodeSheet(
    value: String,
    onValueChange: ClickEventOneArg<String>,
    onSubmit: ClickEvent,
    onDismiss: ClickEvent,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current

    BottomSheet(
        title = stringResource(id = R.string.scanner_enter_manually),
        onDismiss = onDismiss,
        onNavigationClick = onDismiss,
        isFullscreen = false,
        wrapContentHeight = true,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = theme.spacing.spacing16,
                    end = theme.spacing.spacing16,
                    bottom = theme.spacing.spacing24
                )
        ) {
            TextField(
                value = value,
                placeholder = stringResource(id = R.string.scanner_manual_placeholder),
                type = TextFieldType.Default,
                onTextChange = onValueChange,
                isMandatory = false,
                // Barcodes are digits, and the design's key reads "Search" rather than a newline.
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
