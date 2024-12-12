package au.com.alfie.ecomm.designsystem.component.swatch

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.theme.Theme

private val INTERNAL_SMALL_SIZE = 20.dp
private val INTERNAL_LARGE_SIZE = 37.dp
private val BORDER_SIZE_SMALL = 1.dp
private val BORDER_SIZE_LARGE = 2.dp

enum class SwatchSize(
    val externalSize: Dp,
    val internalSize: Dp,
    val borderWidth: Dp
) {
    Small(
        externalSize = Theme.iconSize.medium,
        internalSize = INTERNAL_SMALL_SIZE,
        borderWidth = BORDER_SIZE_SMALL
    ),
    Large(
        externalSize = Theme.iconSize.xLarge,
        internalSize = INTERNAL_LARGE_SIZE,
        borderWidth = BORDER_SIZE_LARGE
    )
}
