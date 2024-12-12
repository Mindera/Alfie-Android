package au.com.alfie.ecomm.core.ui.system

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
interface SystemUiController {

    var isStatusBarVisible: Boolean

    fun setSystemUiColors(
        componentActivity: ComponentActivity,
        statusBarLight: Color,
        navigationLight: Color,
        statusBarDark: Color = statusBarLight,
        navigationDark: Color = navigationLight
    )
}
