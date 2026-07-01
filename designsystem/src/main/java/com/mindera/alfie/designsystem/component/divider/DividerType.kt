package com.mindera.alfie.designsystem.component.divider

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mindera.alfie.designsystem.tokens.LocalTheme

enum class DividerType(val thickness: Dp) {
    Solid1Mono100(thickness = 1.dp),
    Solid1Mono200(thickness = 1.dp),
    Solid1Mono300(thickness = 1.dp),
    Solid2Mono100(thickness = 2.dp)
}

@Composable
fun DividerType.color(): Color {
    val c = LocalTheme.current.primitive.colors
    return when (this) {
        DividerType.Solid1Mono100, DividerType.Solid2Mono100 -> c.neutrals100
        DividerType.Solid1Mono200 -> c.neutrals200
        DividerType.Solid1Mono300 -> c.neutrals300
    }
}
