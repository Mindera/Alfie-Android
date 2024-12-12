package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.input.TextField
import au.com.alfie.ecomm.designsystem.component.input.TextFieldIconData
import au.com.alfie.ecomm.designsystem.component.input.TextFieldSupportComponent
import au.com.alfie.ecomm.designsystem.component.input.TextFieldType
import au.com.alfie.ecomm.designsystem.component.switch.Switch
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import au.com.alfie.ecomm.designsystem.R as RD

@Destination
@Composable
fun InputScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    var isEnabled by remember { mutableStateOf(true) }
    var isMandatory by remember { mutableStateOf(true) }
    var showLabel by remember { mutableStateOf(true) }
    var showCounter by remember { mutableStateOf(true) }
    var showTailingIcon by remember { mutableStateOf(true) }
    var showHint by remember { mutableStateOf(true) }
    var isHintLongText by remember { mutableStateOf(true) }
    var showHintIcon by remember { mutableStateOf(true) }
    var isFocusedDefault by remember { mutableStateOf(false) }
    var isFocusedError by remember { mutableStateOf(false) }
    var isFocusedSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Theme.spacing.spacing16)
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        HeaderDivider(text = "Properties")

        SwitchItem(
            text = "Enabled",
            isChecked = isEnabled,
            onCheckChange = { isEnabled = it }
        )
        SwitchItem(
            text = "Mandatory",
            isChecked = isMandatory,
            onCheckChange = { isMandatory = it }
        )
        SwitchItem(
            text = "Label",
            isChecked = showLabel,
            onCheckChange = { showLabel = it }
        )
        SwitchItem(
            text = "Counter",
            isChecked = showCounter,
            onCheckChange = { showCounter = it }
        )
        SwitchItem(
            text = "Trailing Icon",
            isChecked = showTailingIcon,
            onCheckChange = { showTailingIcon = it }
        )
        SwitchItem(
            text = "Hint",
            isChecked = showHint,
            onCheckChange = { showHint = it }
        )
        SwitchItem(
            text = "Hint Long Text",
            isChecked = isHintLongText,
            onCheckChange = { isHintLongText = it }
        )
        SwitchItem(
            text = "Hint Icon",
            isChecked = showHintIcon,
            onCheckChange = { showHintIcon = it }
        )

        HeaderDivider(text = "Input")

        TextFieldType.entries.forEachIndexed { index, type ->
            InputItem(
                type = type,
                isEnabled = isEnabled,
                isMandatory = isMandatory,
                showLabel = showLabel,
                showCounter = showCounter,
                showTrailingIcon = showTailingIcon,
                showSupportText = showHint,
                isHintLongText = isHintLongText,
                showSupportIcon = showHintIcon,
                onFocusChange = { isFocused ->
                    when (index) {
                        0 -> isFocusedDefault = isFocused
                        1 -> isFocusedError = isFocused
                        else -> isFocusedSuccess = isFocused
                    }
                }
            )
        }
    }
}

@Composable
private fun HeaderDivider(text: String) {
    Text(
        modifier = Modifier.padding(Theme.spacing.spacing12),
        text = text,
        style = Theme.typography.heading3
    )
    HorizontalDivider()
    Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
}

@Composable
private fun SwitchItem(
    text: String,
    isChecked: Boolean,
    onCheckChange: ClickEventOneArg<Boolean>
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(
            checked = isChecked,
            onCheckChange = onCheckChange
        )
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = text,
            style = Theme.typography.paragraph
        )
    }
}

@Composable
private fun InputItem(
    type: TextFieldType,
    isEnabled: Boolean,
    isMandatory: Boolean,
    showLabel: Boolean,
    showCounter: Boolean,
    showTrailingIcon: Boolean,
    showSupportText: Boolean,
    isHintLongText: Boolean,
    showSupportIcon: Boolean,
    onFocusChange: ClickEventOneArg<Boolean>
) {
    var textValue by remember { mutableStateOf("") }
    val title = when (type) {
        TextFieldType.Default -> "Default"
        TextFieldType.Error -> "Error"
        TextFieldType.Success -> "Success"
    }
    val label = if (showLabel) "Label" else null
    val trailingIconData = if (showTrailingIcon) {
        TextFieldIconData(
            icon = RD.drawable.ic_action_chevron_down,
            iconContentDescription = null,
            onIconClickEvent = {}
        )
    } else {
        null
    }
    val supportIcon = when (type) {
        TextFieldType.Error -> RD.drawable.ic_informational_warning
        TextFieldType.Default -> RD.drawable.ic_informational_info
        TextFieldType.Success -> RD.drawable.ic_informational_checkmark
    }
    val supportText = if (isHintLongText) {
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
    } else {
        "Support Text"
    }
    val supportTextFieldSupportComponent = if (showSupportText) {
        TextFieldSupportComponent(
            text = supportText,
            icon = if (showSupportIcon) supportIcon else null,
            iconContentDescription = null
        )
    } else {
        null
    }

    Text(
        text = title,
        style = Theme.typography.paragraph
    )
    Spacer(modifier = Modifier.height(Theme.spacing.spacing8))
    TextField(
        value = textValue,
        placeholder = "Placeholder",
        onTextChange = { term -> textValue = term },
        onFocusChange = onFocusChange,
        type = type,
        isEnabled = isEnabled,
        isMandatory = isMandatory,
        showCounter = showCounter,
        supportComponent = supportTextFieldSupportComponent,
        trailingIconData = trailingIconData,
        label = label
    )
    Spacer(modifier = Modifier.height(Theme.spacing.spacing20))
}

@Preview(showBackground = true)
@Preview
@Composable
private fun InputScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Spacing Screen"),
        showNavigationIcon = false
    )
    Theme {
        InputScreen(topBarState = topBarState)
    }
}
