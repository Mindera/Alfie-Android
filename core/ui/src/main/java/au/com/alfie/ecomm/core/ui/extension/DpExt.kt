package au.com.alfie.ecomm.core.ui.extension

import androidx.compose.ui.unit.Dp

// Following window size guidelines
// https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
private const val WIDTH_COMPACT_LIMIT = 600
private const val WIDTH_MEDIUM_LIMIT = 840
private const val HEIGHT_COMPACT_LIMIT = 480
private const val HEIGHT_MEDIUM_LIMIT = 900

fun Dp.widthWindowType(): WindowType = when {
    value < WIDTH_COMPACT_LIMIT -> WindowType.Compact
    value < WIDTH_MEDIUM_LIMIT -> WindowType.Medium
    else -> WindowType.Expanded
}

fun Dp.heightWindowType(): WindowType = when {
    value < HEIGHT_COMPACT_LIMIT -> WindowType.Compact
    value < HEIGHT_MEDIUM_LIMIT -> WindowType.Medium
    else -> WindowType.Expanded
}

enum class WindowType {
    Compact,
    Medium,
    Expanded
}
