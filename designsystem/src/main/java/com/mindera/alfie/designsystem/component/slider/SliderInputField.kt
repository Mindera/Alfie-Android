package com.mindera.alfie.designsystem.component.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

/**
 * The numeric bound input that sits below a [RangeSlider], per Design System node `3001:8401`.
 *
 * In the DS these inputs belong to the Slider itself — it exposes a `showInputs` boolean rather than
 * modelling them separately — so they live alongside it here rather than reusing
 * `component/input/TextField`, which is a different component with a label, mandatory marker and
 * support row that the DS slider input does not have.
 *
 * [prefix] renders in the same style as the value, not dimmed, which is how the DS draws the currency
 * symbol.
 */
@Composable
fun SliderInputField(
    value: String,
    onValueChange: ClickEventOneArg<String>,
    modifier: Modifier = Modifier,
    prefix: String? = null,
    /**
     * Names the field for screen readers. The DS draws no visible label, and when a bound is
     * unset the field shows only the currency prefix, so without this the two ends of a range are
     * indistinguishable to TalkBack.
     */
    contentDescription: String? = null,
    isEnabled: Boolean = true
) {
    val theme = LocalTheme.current
    val textStyle = theme.typography.body.medium.copy(color = theme.color.content.contentPrimary)

    Row(
        modifier = modifier
            .background(
                color = theme.color.surface.backgroundPrimary,
                shape = theme.sizing.radius.soft
            )
            .border(
                width = theme.primitive.border.weightDefault,
                color = theme.color.border.soft,
                shape = theme.sizing.radius.soft
            )
            // Asymmetric by design: the DS uses a 12 dp leading inset with a 16 dp trailing one.
            .padding(
                start = INPUT_PADDING_START,
                end = theme.spacing.spacing16,
                top = theme.spacing.spacing8,
                bottom = theme.spacing.spacing8
            ),
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        prefix?.let {
            Text(
                text = it,
                style = textStyle
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = isEnabled,
            singleLine = true,
            textStyle = textStyle,
            cursorBrush = SolidColor(theme.color.content.contentPrimary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                .semantics {
                    contentDescription?.let { this.contentDescription = it }
                }
        )
    }
}

/** The DS leading inset is 12, which has no spacing token — the scale jumps 8 -> 16. */
private val INPUT_PADDING_START = 12.dp

@Preview(showBackground = true)
@Composable
private fun SliderInputFieldPreview() {
    Theme {
        Row(horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16)) {
            SliderInputField(
                value = "40",
                onValueChange = {},
                prefix = "$",
                modifier = Modifier.weight(1f)
            )
            SliderInputField(
                value = "120",
                onValueChange = {},
                prefix = "$",
                modifier = Modifier.weight(1f)
            )
        }
    }
}
