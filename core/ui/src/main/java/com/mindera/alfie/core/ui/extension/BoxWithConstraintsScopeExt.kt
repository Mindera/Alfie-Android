package com.mindera.alfie.core.ui.extension

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.ui.extension.WindowType.Compact
import com.mindera.alfie.core.ui.extension.WindowType.Expanded
import com.mindera.alfie.core.ui.extension.WindowType.Medium

@Composable
fun <T> BoxWithConstraintsScope.handleWindowType(
    onCompactWindow: @Composable () -> T,
    onNotCompactWindow: @Composable () -> T
): T = if (maxWidth.widthWindowType() == Compact) {
    onCompactWindow()
} else {
    onNotCompactWindow()
}

@Composable
fun <T> BoxWithConstraintsScope.handleWindowType(
    onCompactWindow: @Composable () -> T,
    onMediumWindow: @Composable () -> T,
    onExpandedWindow: @Composable () -> T
): T = when (maxWidth.widthWindowType()) {
    Compact -> onCompactWindow()
    Medium -> onMediumWindow()
    Expanded -> onExpandedWindow()
}

fun WindowType.getDialogWidth(): Dp = when (this) {
    Compact -> 342.dp
    Medium -> 536.dp
    Expanded -> 436.dp
}
