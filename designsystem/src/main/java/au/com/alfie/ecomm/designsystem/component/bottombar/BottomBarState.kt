package au.com.alfie.ecomm.designsystem.component.bottombar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberBottomBarState(
    isVisible: Boolean = true
): BottomBarState = remember {
    BottomBarState(isVisible = isVisible)
}

@Stable
class BottomBarState(isVisible: Boolean) {

    var isVisible by mutableStateOf(isVisible)
        private set

    fun hideBottomBar() {
        this.isVisible = false
    }

    fun showBottomBar() {
        this.isVisible = true
    }
}
