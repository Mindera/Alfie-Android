package au.com.alfie.ecomm.core.navigation

import androidx.compose.runtime.Stable
import com.ramcosta.composedestinations.spec.Direction

@Stable
interface DirectionProvider {
    fun fromScreen(screen: Screen): Direction
}
