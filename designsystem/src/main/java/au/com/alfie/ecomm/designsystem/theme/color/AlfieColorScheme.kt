package au.com.alfie.ecomm.designsystem.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
internal fun alfieColorScheme(): ColorScheme = lightColorScheme(
    primary = Theme.color.primary.mono700,
    secondary = Theme.color.secondary.green700,
    tertiary = Theme.color.secondary.blue700,
    background = Theme.color.white,
    surface = Theme.color.white,
    error = Theme.color.secondary.red700
)
