package au.com.alfie.ecomm.designsystem.component.overlay

import androidx.compose.runtime.Immutable

@Immutable
data class OverlayProperties(
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true
)
