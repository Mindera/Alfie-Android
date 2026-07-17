package com.mindera.alfie.designsystem.component.input

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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.animation.DefaultVisibilityAnimation
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

// TEXT_FIELD_MINIMUM_HEIGHT derived from: body.medium line-height (24dp) + 2 × spacing8 (vertical padding) = 40dp
private const val TEXT_FIELD_MINIMUM_HEIGHT_DP = 40
private const val MAX_CHARACTERS = 100
private const val REQUIRED_LABEL = "*"

@Composable
fun TextField(
    value: String,
    placeholder: String,
    type: TextFieldType,
    onTextChange: ClickEventOneArg<String>,
    modifier: Modifier = Modifier,
    label: String? = null,
    isMandatory: Boolean = true,
    isEnabled: Boolean = true,
    onFocusChange: ClickEventOneArg<Boolean> = {},
    supportComponent: TextFieldSupportComponent? = null,
    trailingIconData: TextFieldIconData? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val theme = LocalTheme.current
    val c = theme.primitive.colors
    val typeColors = type.colorSpec()
    val disabledColor = c.neutrals200
    val labelColor = c.neutrals500
    val labelRequiredColor = c.semanticError800
    val placeholderColor = c.neutrals500
    val inputTextColor = c.neutrals800
    val trailingIconColorDefault = c.neutrals800

    var isFocused by remember { mutableStateOf(false) }

    val labelTextColor = animateColorAsState(
        targetValue = if (isEnabled) labelColor else disabledColor,
        label = "Label Color Animation"
    )
    val labelRequiredTextColor = animateColorAsState(
        targetValue = if (isEnabled) labelRequiredColor else disabledColor,
        label = "Label Required Text Color Animation"
    )
    val borderColor = animateColorAsState(
        targetValue = when {
            isFocused -> typeColors.inputBorderFocusedColor
            isEnabled -> typeColors.inputBorderColor
            else -> disabledColor
        },
        label = "Border Color Animation"
    )
    val inputTextColorState = animateColorAsState(
        targetValue = if (isEnabled) inputTextColor else disabledColor,
        label = "Input Text Color Animation"
    )
    val placeholderTextColor = animateColorAsState(
        targetValue = if (isEnabled) placeholderColor else disabledColor,
        label = "Input Placeholder Text Color Animation"
    )
    val trailingIconColor = animateColorAsState(
        targetValue = if (isEnabled) trailingIconColorDefault else disabledColor,
        label = "Input Trailing Icon Color Animation"
    )
    val supportTextColor = animateColorAsState(
        targetValue = if (isEnabled) typeColors.supportTextColor else disabledColor,
        label = "Support Text Color Animation"
    )
    val supportIconColor = animateColorAsState(
        targetValue = if (isEnabled) typeColors.supportIconColor else disabledColor,
        label = "Support Icon Color Animation"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        // D2: outer gap uses spacing4 (label ↔ box gap from Figma spec)
        verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing4)
    ) {
        // D2: render LabelRow only when there is a label or mandatory marker to show
        if (label != null || isMandatory) {
            LabelRow(
                isMandatory = isMandatory,
                label = label.orEmpty(),
                labelTextColor = labelTextColor.value,
                labelRequiredTextColor = labelRequiredTextColor.value
            )
        }

        TextField(
            value = value,
            isEnabled = isEnabled,
            onFocusChange = { focus ->
                isFocused = focus
                onFocusChange(focus)
            },
            onTextChange = { term ->
                // Keep MAX_CHARACTERS input cap; counter UI removed (D3)
                if (term.length <= MAX_CHARACTERS) {
                    onTextChange(term)
                }
            },
            borderColor = borderColor.value,
            inputTextColor = inputTextColorState.value,
            placeholderText = placeholder,
            placeholderTextColor = placeholderTextColor.value,
            trailingIconData = trailingIconData,
            trailingIconColor = trailingIconColor.value,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation
        )

        // D2/D4: render SupportTextRow only when a support component is provided; no reserved space
        if (supportComponent != null) {
            SupportTextRow(
                supportComponent = supportComponent,
                supportIconColor = supportIconColor.value,
                supportTextColor = supportTextColor.value
            )
        }
    }
}

@Composable
private fun LabelRow(
    isMandatory: Boolean,
    label: String,
    labelTextColor: Color,
    labelRequiredTextColor: Color
) {
    val theme = LocalTheme.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing4)
    ) {
        val modifierLabel = if (isMandatory.not()) Modifier.weight(1F) else Modifier
        Text(
            text = label,
            style = theme.typography.body.medium,
            maxLines = 1,
            overflow = Ellipsis,
            color = labelTextColor,
            modifier = modifierLabel
        )
        // D5: mandatory * kept as code-only, conditional on isMandatory
        if (isMandatory) {
            Text(
                text = REQUIRED_LABEL,
                style = theme.typography.body.medium,
                color = labelRequiredTextColor,
                modifier = Modifier.weight(1F)
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
    val theme = LocalTheme.current
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        modifier = Modifier
            .fillMaxWidth()
            // Height derived from body.medium line-height (24dp) + 2×spacing8; kept as literal with comment above
            .heightIn(min = TEXT_FIELD_MINIMUM_HEIGHT_DP.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                onFocusChange(focusState.isFocused)
            }
            .border(
                // D7/D9: was 1.5.dp literal → primitive.border.weightDefault (1dp)
                width = theme.primitive.border.weightDefault,
                color = borderColor,
                // D7/D9: was Theme.shape.extraSmall → sizing.radius.soft (both = RoundedCornerShape(4dp))
                shape = theme.sizing.radius.soft
            ),
        // D7/D9: was Theme.typography.paragraph → typography.body.medium
        textStyle = theme.typography.body.medium.copy(color = inputTextColor),
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
                    // D7/D9: was padding(horizontal = Theme.spacing.spacing20) → start=spacing12, end=spacing16
                    .padding(
                        start = theme.spacing.spacing12,
                        end = theme.spacing.spacing16,
                        top = theme.spacing.spacing8,
                        bottom = theme.spacing.spacing8
                    )
                    .indication(
                        interactionSource = interactionSource,
                        indication = ripple(bounded = false)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                // D7: gap between input and trailing icon
                horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing8)
            ) {
                Box(
                    modifier = Modifier.weight(1F)
                ) {
                    DefaultVisibilityAnimation(isVisible = value.isEmpty()) {
                        Text(
                            text = placeholderText,
                            // D7/D9: was Theme.typography.paragraph → typography.body.medium
                            style = theme.typography.body.medium,
                            color = placeholderTextColor,
                            maxLines = 1
                        )
                    }
                    innerTextField()
                }

                DefaultVisibilityAnimation(isVisible = trailingIconData != null) {
                    if (trailingIconData != null) {
                        IconButton(
                            // D9: was Theme.iconSize.medium → sizing.icon.medium
                            modifier = Modifier.size(theme.sizing.icon.medium),
                            enabled = isEnabled,
                            onClick = {
                                trailingIconData.onIconClickEvent()
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    // D9: was Theme.iconSize.small → sizing.icon.small
                                    .size(theme.sizing.icon.small)
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
    supportComponent: TextFieldSupportComponent,
    supportIconColor: Color,
    supportTextColor: Color
) {
    val theme = LocalTheme.current
    // D2: no heightIn(min = 20.dp) reserved space — row only renders when supportComponent != null
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing2)
    ) {
        val icon = supportComponent.icon
        if (icon != null) {
            Icon(
                // D9: was Theme.iconSize.small → sizing.icon.small
                modifier = Modifier.size(theme.sizing.icon.small),
                painter = painterResource(id = icon),
                tint = supportIconColor,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(theme.spacing.spacing2))
        }
        Text(
            text = supportComponent.text,
            // D7/D9: was Theme.typography.small → typography.body.small
            style = theme.typography.body.small,
            maxLines = 2,
            overflow = Ellipsis,
            color = supportTextColor
        )
    }
}

@Preview(showBackground = true, name = "TextField - basic box (null extras)")
@Composable
private fun TextFieldPreviewBasicBox() {
    Theme {
        TextField(
            value = "",
            placeholder = "Placeholder",
            type = TextFieldType.Default,
            onTextChange = {},
            label = null,
            isMandatory = false
        )
    }
}

@Preview(showBackground = true, name = "TextField - label only")
@Composable
private fun TextFieldPreviewLabelOnly() {
    Theme {
        TextField(
            value = "",
            label = "Label",
            placeholder = "Placeholder",
            type = TextFieldType.Default,
            onTextChange = {}
        )
    }
}

@Preview(showBackground = true, name = "TextField - label + helper")
@Composable
private fun TextFieldPreviewLabelHelper() {
    Theme {
        TextField(
            value = "",
            label = "Label",
            placeholder = "Placeholder",
            type = TextFieldType.Default,
            onTextChange = {},
            supportComponent = TextFieldSupportComponent("Hint text")
        )
    }
}

@Preview(showBackground = true, name = "TextField - Error")
@Composable
private fun TextFieldPreviewError() {
    Theme {
        TextField(
            value = "Input text",
            label = "Label",
            placeholder = "Placeholder",
            type = TextFieldType.Error,
            onTextChange = {},
            supportComponent = TextFieldSupportComponent("Error message")
        )
    }
}

@Preview(showBackground = true, name = "TextField - Success")
@Composable
private fun TextFieldPreviewSuccess() {
    Theme {
        TextField(
            value = "Input text",
            label = "Label",
            placeholder = "Placeholder",
            type = TextFieldType.Success,
            onTextChange = {},
            supportComponent = TextFieldSupportComponent("Success message")
        )
    }
}

@Preview(showBackground = true, name = "TextField - Disabled")
@Composable
private fun TextFieldPreviewDisabled() {
    Theme {
        TextField(
            value = "Input text",
            label = "Label",
            placeholder = "Placeholder",
            type = TextFieldType.Default,
            isEnabled = false,
            onTextChange = {},
            supportComponent = TextFieldSupportComponent("Hint text")
        )
    }
}
