package com.mindera.alfie.designsystem.component.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.component.button.ButtonSize.Large
import com.mindera.alfie.designsystem.component.button.ButtonSize.Medium
import com.mindera.alfie.designsystem.component.button.ButtonSize.Small
import com.mindera.alfie.designsystem.component.button.ButtonType.Destructive
import com.mindera.alfie.designsystem.component.button.ButtonType.Primary
import com.mindera.alfie.designsystem.component.button.ButtonType.Secondary
import com.mindera.alfie.designsystem.component.button.ButtonType.Tertiary
import com.mindera.alfie.designsystem.component.button.ButtonType.Underlined
import com.mindera.alfie.designsystem.component.button.IconPosition.Left
import com.mindera.alfie.designsystem.component.button.IconPosition.Right
import com.mindera.alfie.designsystem.component.loading.Loading
import com.mindera.alfie.designsystem.component.shimmer.shimmer
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

private val BORDER_THICKNESS = 1.dp

@Composable
fun Button(
    type: ButtonType,
    text: String,
    onClick: ClickEvent,
    modifier: Modifier = Modifier,
    buttonSize: ButtonSize = Small,
    iconButton: IconButton? = null,
    shape: RoundedCornerShape = Theme.shape.none,
    isLoading: Boolean = false,
    isShimmering: Boolean = false,
    isEnabled: Boolean = true,
    overrideTextStyle: TextStyle? = null,
    overrideBorderThickness: Dp? = null,
    overrideColors: ButtonColors? = null,
    overrideTextColor: Color? = null,
    overrideTextDisabledColor: Color? = null
) {
    val spec = type.colorSpec()

    val borderColor by animateColorAsState(
        targetValue = if (isEnabled) {
            overrideColors?.contentColor ?: spec.border
        } else {
            overrideColors?.disabledContentColor ?: spec.disabledBorder
        },
        label = "border color"
    )
    val border = if (type.hasBorder) {
        BorderStroke(
            width = overrideBorderThickness ?: BORDER_THICKNESS,
            color = borderColor
        )
    } else {
        null
    }

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = overrideColors?.containerColor ?: spec.background,
        contentColor = overrideColors?.contentColor ?: spec.text,
        disabledContainerColor = overrideColors?.disabledContainerColor ?: spec.disabledBackground,
        disabledContentColor = overrideColors?.disabledContentColor ?: spec.disabledText
    )

    val textColor by animateColorAsState(
        targetValue = if (isEnabled) {
            overrideTextColor ?: overrideColors?.contentColor ?: spec.text
        } else {
            overrideTextDisabledColor ?: overrideColors?.disabledContentColor ?: spec.disabledText
        },
        label = "text color"
    )

    if (type != Underlined) {
        val textStyle = overrideTextStyle ?: LocalTheme.current.typography.body.medium

        NormalButton(
            modifier = modifier,
            text = text,
            type = type,
            spec = spec,
            buttonSize = buttonSize,
            buttonColors = buttonColors,
            iconButton = iconButton,
            borderStroke = border,
            shape = shape,
            isLoading = isLoading,
            isShimmering = isShimmering,
            isEnabled = isEnabled,
            onClick = onClick,
            textColor = textColor,
            textStyle = textStyle
        )
    } else {
        val textStyle = overrideTextStyle
            ?: LocalTheme.current.typography.body.medium.copy(textDecoration = TextDecoration.Underline)

        UnderlineButton(
            modifier = modifier,
            text = text,
            isShimmering = isShimmering,
            isEnabled = isEnabled,
            onClick = onClick,
            textColor = textColor,
            textStyle = textStyle
        )
    }
}

@Composable
private fun NormalButton(
    text: String,
    type: ButtonType,
    spec: ButtonColorSpec,
    buttonSize: ButtonSize,
    buttonColors: ButtonColors,
    iconButton: IconButton?,
    borderStroke: BorderStroke?,
    shape: RoundedCornerShape,
    isLoading: Boolean,
    isShimmering: Boolean,
    isEnabled: Boolean,
    onClick: ClickEvent,
    textColor: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    Button(
        modifier = modifier
            .height(buttonSize.height)
            .shimmer(
                isShimmering = isShimmering,
                colors = type.shimmerColors,
                shape = shape
            ),
        onClick = onClick,
        enabled = isEnabled,
        border = borderStroke,
        shape = shape,
        colors = buttonColors,
        contentPadding = PaddingValues(horizontal = buttonSize.horizontalPadding)
    ) {
        Box {
            val visibility by animateFloatAsState(
                targetValue = if (isLoading) 0f else 1f,
                label = "visibility"
            )

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .alpha(visibility),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val iconTint = if (isEnabled) spec.icon else spec.disabledIcon
                if (iconButton != null && iconButton.position == Left) {
                    Icon(
                        painter = painterResource(id = iconButton.iconResource),
                        contentDescription = iconButton.contentDescription,
                        tint = iconTint,
                        modifier = Modifier
                            .size(theme.sizing.icon.medium)
                            .padding(end = theme.spacing.spacing8)
                    )
                }

                Text(
                    text = text,
                    textAlign = Center,
                    maxLines = 1,
                    overflow = Ellipsis,
                    color = textColor,
                    style = textStyle
                )
                if (iconButton != null && iconButton.position == Right) {
                    Icon(
                        painter = painterResource(id = iconButton.iconResource),
                        contentDescription = iconButton.contentDescription,
                        tint = iconTint,
                        modifier = Modifier
                            .size(theme.sizing.icon.medium)
                            .padding(start = theme.spacing.spacing8)
                    )
                }
            }
            if (isLoading) {
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.Center)
                ) {
                    val loadingType = if (isEnabled) type.loadingType else type.disabledLoadingType
                    val color = if (isEnabled) spec.background else spec.disabledBackground
                    Loading(
                        modifier = Modifier.background(color),
                        type = loadingType
                    )
                }
            }
        }
    }
}

@Composable
private fun UnderlineButton(
    text: String,
    isShimmering: Boolean,
    isEnabled: Boolean,
    onClick: ClickEvent,
    textColor: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier
            .shimmer(isShimmering)
            .clickable(enabled = isEnabled) { onClick() }
            .padding(LocalTheme.current.spacing.spacing8),
        text = text,
        textAlign = Center,
        maxLines = 1,
        overflow = Ellipsis,
        color = textColor,
        style = textStyle
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ButtonPreview() {
    Theme {
        Column {
            Button(
                type = Primary,
                onClick = {},
                isEnabled = true,
                text = "Primary Default"
            )
            Button(
                type = Primary,
                onClick = {},
                isEnabled = true,
                text = "Primary Default",
                isLoading = true
            )
            Button(
                type = Primary,
                onClick = {},
                isEnabled = false,
                text = "Primary Default",
                isLoading = true
            )
            Button(
                type = Primary,
                onClick = {},
                isEnabled = true,
                text = "Primary Medium",
                buttonSize = Medium
            )
            Button(
                type = Primary,
                onClick = {},
                isEnabled = true,
                text = "Primary Large",
                buttonSize = Large
            )
            Button(
                type = Primary,
                onClick = {},
                isEnabled = true,
                text = "Primary",
                iconButton = IconButton(iconResource = AlfieIcons.Star, position = Left)
            )
            Button(
                type = Primary,
                onClick = {},
                isEnabled = false,
                text = "Primary",
                iconButton = IconButton(iconResource = AlfieIcons.Forward, position = Right)
            )
            Button(
                type = Primary,
                onClick = {},
                isEnabled = false,
                text = "Primary Disabled"
            )
            Button(
                type = Secondary,
                onClick = {},
                isEnabled = true,
                text = "Secondary Default"
            )
            Button(
                type = Secondary,
                onClick = {},
                isEnabled = false,
                text = "Secondary Disabled"
            )
            Button(
                type = Tertiary,
                onClick = {},
                isEnabled = true,
                text = "Tertiary Default"
            )
            Button(
                type = Tertiary,
                onClick = {},
                isEnabled = false,
                text = "Tertiary Disabled"
            )
            Button(
                type = Destructive,
                onClick = {},
                isEnabled = true,
                text = "Destructive Default"
            )
            Button(
                type = Destructive,
                onClick = {},
                isEnabled = false,
                text = "Destructive Disabled"
            )
            Button(
                type = Underlined,
                onClick = {},
                isEnabled = true,
                text = "Underlined Default"
            )
            Button(
                type = Underlined,
                onClick = {},
                isEnabled = false,
                text = "Underlined Disabled"
            )
        }
    }
}
