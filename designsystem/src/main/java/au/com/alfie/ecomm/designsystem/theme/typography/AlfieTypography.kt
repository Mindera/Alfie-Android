package au.com.alfie.ecomm.designsystem.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
internal fun alfieTypography(): Typography = Typography(
    displayLarge = Theme.typography.paragraph,
    displayMedium = Theme.typography.paragraph,
    displaySmall = Theme.typography.paragraph,
    headlineLarge = Theme.typography.heading1,
    headlineMedium = Theme.typography.heading2,
    headlineSmall = Theme.typography.heading3,
    bodyLarge = Theme.typography.paragraph,
    bodyMedium = Theme.typography.paragraph,
    bodySmall = Theme.typography.paragraph,
    labelLarge = Theme.typography.small,
    labelMedium = Theme.typography.small,
    labelSmall = Theme.typography.small
)
