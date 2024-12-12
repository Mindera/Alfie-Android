package au.com.alfie.ecomm.designsystem.component.shimmer

import androidx.compose.ui.graphics.Color
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

enum class ShimmerColors(val colors: ImmutableList<Color>) {
    Light(
        persistentListOf(
            Theme.color.primary.mono200,
            Theme.color.primary.mono300,
            Theme.color.primary.mono200
        )
    ),
    Dark(
        persistentListOf(
            Theme.color.primary.mono600,
            Theme.color.primary.mono900,
            Theme.color.primary.mono600
        )
    )
}
