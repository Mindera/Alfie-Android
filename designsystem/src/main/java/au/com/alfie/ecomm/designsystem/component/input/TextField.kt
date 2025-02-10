package au.com.alfie.ecomm.designsystem.component.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.animation.DefaultVisibilityAnimation
import au.com.alfie.ecomm.designsystem.theme.Theme

private val TEXT_FIELD_MINIMUM_HEIGHT = 40.dp
private val SUPPORT_TEXT_MINIMUM_HEIGHT = 20.dp
private val TEXT_FIELD_BORDER_WIDTH = 1.5.dp
private const val REQUIRED_LABEL = "*"
private val DISABLED_COLOR = Theme.color.primary.mono200
private val LABEL_COLOR = Theme.color.primary.mono500
private val LABEL_REQUIRED_COLOR = Theme.color.secondary.red800
private val COUNTER_COLOR = Theme.color.primary.mono500
private val PLACEHOLDER_COLOR = Theme.color.primary.mono500
private val INPUT_TEXT_COLOR = Theme.color.primary.mono900
private val TRAILING_ICON_COLOR = Theme.color.primary.mono900
private const val MAX_CHARACTERS = 100

@Composable
fun TextField(
    value: String,
    placeholder: String,
    type: TextFieldType,
    onTextChange: ClickEventOneArg<String>,
    modifier: Modifier = Modifier,
    label: String? = null,
    isMandatory: Boolean = true,
    showCounter: Boolean = false,
    isEnabled: Boolean = true,
    onFocusChange: ClickEventOneArg<Boolean> = {},
    supportComponent: TextFieldSupportComponent? = null,
    trailingIconData: TextFieldIconData? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var counterValue by remember { mutableIntStateOf(0) }
    var isFocused by remember { mutableStateOf(false) }

    val labelTextColor = animateColorAsState(
        targetValue = if (isEnabled) LABEL_COLOR else DISABLED_COLOR,
        label = "Label Color Animation"
    )
    val labelRequiredTextColor = animateColorAsState(
        targetValue = if (isEnabled) LABEL_REQUIRED_COLOR else DISABLED_COLOR,
        label = "Label Required Text Color Animation"
    )
    val counterTextColor = animateColorAsState(
        targetValue = if (isEnabled) COUNTER_COLOR else DISABLED_COLOR,
        label = "Counter Text Color Animation"
    )
    val borderColor = animateColorAsState(
        targetValue = when {
            isFocused -> type.inputBorderFocusedColor
            isEnabled -> type.inputBorderColor
            else -> DISABLED_COLOR
        },
        label = "Border Color Animation"
    )
    val inputTextColor = animateColorAsState(
        targetValue = if (isEnabled) INPUT_TEXT_COLOR else DISABLED_COLOR,
        label = "Input Text Color Animation"
    )
    val placeholderTextColor = animateColorAsState(
        targetValue = if (isEnabled) PLACEHOLDER_COLOR else DISABLED_COLOR,
        label = "Input Placeholder Text Color Animation"
    )
    val trailingIconColor = animateColorAsState(
        targetValue = if (isEnabled) TRAILING_ICON_COLOR else DISABLED_COLOR,
        label = "Input Trailing Icon Color Animation"
    )
    val supportTextColor = animateColorAsState(
        targetValue = if (isEnabled) type.supportTextColor else DISABLED_COLOR,
        label = "Support Text Color Animation"
    )
    val supportIconColor = animateColorAsState(
        targetValue = if (isEnabled) type.supportIconColor else DISABLED_COLOR,
        label = "Support Icon Color Animation"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing8)
    ) {
        LabelRow(
            isMandatory = isMandatory,
            label = label.orEmpty(),
            labelTextColor = labelTextColor.value,
            labelRequiredTextColor = labelRequiredTextColor.value,
            showCounter = showCounter,
            counterTextColor = counterTextColor.value,
            counterValue = counterValue
        )

        TextField(
            value = value,
            isEnabled = isEnabled,
            onFocusChange = { focus ->
                isFocused = focus
                onFocusChange(focus)
            },
            onTextChange = { term ->
                if (term.length <= MAX_CHARACTERS) {
                    counterValue = term.length
                    onTextChange(term)
                }
            },
            borderColor = borderColor.value,
            inputTextColor = inputTextColor.value,
            placeholderText = placeholder,
            placeholderTextColor = placeholderTextColor.value,
            trailingIconData = trailingIconData,
            trailingIconColor = trailingIconColor.value,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation
        )

        SupportTextRow(
            supportComponent = supportComponent,
            supportIconColor = supportIconColor.value,
            supportTextColor = supportTextColor.value
        )
    }
}

@Composable
private fun LabelRow(
    isMandatory: Boolean,
    label: String,
    labelTextColor: Color,
    labelRequiredTextColor: Color,
    showCounter: Boolean,
    counterTextColor: Color,
    counterValue: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing4)
    ) {
        val modifierLabel = if (isMandatory.not()) Modifier.weight(1F) else Modifier
        Text(
            text = label,
            style = Theme.typography.paragraph,
            maxLines = 1,
            overflow = Ellipsis,
            color = labelTextColor,
            modifier = modifierLabel
        )
        if (isMandatory) {
            Text(
                text = REQUIRED_LABEL,
                style = Theme.typography.paragraph,
                color = labelRequiredTextColor,
                modifier = Modifier.weight(1F)
            )
        }
        if (showCounter) {
            Text(
                text = stringResource(id = R.string.text_field_counter, counterValue),
                style = Theme.typography.paragraph,
                color = counterTextColor
            )
        }
    }
}

@Composable
private fun TextField(
    value: String,
    isEnabled: Boolean,
    onFocusChange: ClickEventOneArg<Boolean>,
    onTextChange: ClickEventOneArg<String>,
    borderColor: Color,
    inputTextColor: Color,
    placeholderText: String,
    placeholderTextColor: Color,
    trailingIconData: TextFieldIconData?,
    trailingIconColor: Color,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation
) {
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = TEXT_FIELD_MINIMUM_HEIGHT)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                onFocusChange(focusState.isFocused)
            }
            .border(
                width = TEXT_FIELD_BORDER_WIDTH,
                color = borderColor,
                shape = Theme.shape.extraSmall
            ),
        textStyle = Theme.typography.paragraph,
        onValueChange = { term ->
            onTextChange(term)
        },
        enabled = isEnabled,
        cursorBrush = SolidColor(inputTextColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(horizontal = Theme.spacing.spacing20)
                    .indication(
                        interactionSource = interactionSource,
                        indication = ripple(bounded = false)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1F)
                ) {
                    DefaultVisibilityAnimation(isVisible = value.isEmpty()) {
                        Text(
                            text = placeholderText,
                            style = Theme.typography.paragraph,
                            color = placeholderTextColor,
                            maxLines = 1
                        )
                    }
                    innerTextField()
                }

                DefaultVisibilityAnimation(isVisible = trailingIconData != null) {
                    if (trailingIconData != null) {
                        IconButton(
                            modifier = Modifier.size(Theme.iconSize.medium),
                            enabled = isEnabled,
                            onClick = {
                                trailingIconData.onIconClickEvent()
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(Theme.iconSize.small)
                                    .align(Alignment.CenterVertically),
                                painter = painterResource(id = trailingIconData.icon),
                                contentDescription = trailingIconData.iconContentDescription,
                                tint = trailingIconColor
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun SupportTextRow(
    supportComponent: TextFieldSupportComponent?,
    supportIconColor: Color,
    supportTextColor: Color
) {
    Row(
        modifier = Modifier.heightIn(min = SUPPORT_TEXT_MINIMUM_HEIGHT),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing2)
    ) {
        val icon = supportComponent?.icon
        if (icon != null) {
            Icon(
                modifier = Modifier.size(Theme.iconSize.small),
                painter = painterResource(id = icon),
                tint = supportIconColor,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(Theme.spacing.spacing2))
        }
        Text(
            text = supportComponent?.text.orEmpty(),
            style = Theme.typography.small,
            maxLines = 2,
            overflow = Ellipsis,
            color = supportTextColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldPreview() {
    Theme {
        TextField(
            value = "",
            label = "Label",
            placeholder = "Placeholder",
            type = TextFieldType.Default,
            onTextChange = {},
            supportComponent = TextFieldSupportComponent("Hint")
        )
    }
}
