package com.mindera.alfie.designsystem.component.overlay

import androidx.compose.runtime.Immutable

@Immutable
data class OverlayProperties(
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true
)
