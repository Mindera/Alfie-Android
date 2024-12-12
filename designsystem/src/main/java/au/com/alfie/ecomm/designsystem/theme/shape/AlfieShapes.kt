package au.com.alfie.ecomm.designsystem.theme.shape

import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
internal fun alfieShapes(): Shapes = Shapes(
    extraSmall = Theme.shape.extraSmall,
    small = Theme.shape.small,
    medium = Theme.shape.medium,
    large = Theme.shape.large,
    extraLarge = Theme.shape.extraLarge
)
