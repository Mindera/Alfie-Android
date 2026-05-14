package com.mindera.alfie.core.navigation

import androidx.compose.runtime.Stable
import com.ramcosta.composedestinations.spec.Direction

@Stable
interface DirectionProvider {
    fun fromScreen(screen: Screen): Direction
}
