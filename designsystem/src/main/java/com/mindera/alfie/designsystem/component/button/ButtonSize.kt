package com.mindera.alfie.designsystem.component.button

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ButtonSize(val height: Dp, val horizontalPadding: Dp, val verticalPadding: Dp) {
    Small(32.dp, 8.dp, 4.dp), // height derived: icon(24) + 2×verticalPadding(4)
    Medium(40.dp, 16.dp, 8.dp), // height derived: icon(24) + 2×verticalPadding(8)
    Large(52.dp, 20.dp, 0.dp) // no Figma spec — kept from legacy
}
