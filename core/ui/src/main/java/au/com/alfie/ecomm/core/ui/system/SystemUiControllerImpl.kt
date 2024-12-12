package au.com.alfie.ecomm.core.ui.system

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

internal class SystemUiControllerImpl(
    private val view: View,
    private val isDarkMode: Boolean,
    window: Window?
) : SystemUiController {

    private val windowInsetsController = window?.let {
        WindowCompat.getInsetsController(it, view)
    }

    override var isStatusBarVisible: Boolean
        get() {
            val windowInsets = ViewCompat.getRootWindowInsets(view)
            return windowInsets?.isVisible(WindowInsetsCompat.Type.statusBars()) == true
        }
        set(value) {
            if (value) {
                windowInsetsController?.show(WindowInsetsCompat.Type.statusBars())
            } else {
                windowInsetsController?.hide(WindowInsetsCompat.Type.statusBars())
            }
        }

    override fun setSystemUiColors(
        componentActivity: ComponentActivity,
        statusBarLight: Color,
        navigationLight: Color,
        statusBarDark: Color,
        navigationDark: Color
    ) {
        componentActivity.enableEdgeToEdge(
            statusBarStyle = if (isDarkMode) {
                SystemBarStyle.dark(statusBarDark.toArgb())
            } else {
                SystemBarStyle.light(
                    statusBarLight.toArgb(),
                    statusBarDark.toArgb()
                )
            },
            navigationBarStyle = if (isDarkMode) {
                SystemBarStyle.dark(navigationDark.toArgb())
            } else {
                SystemBarStyle.light(
                    navigationLight.toArgb(),
                    navigationDark.toArgb()
                )
            }
        )
    }
}

@Composable
fun rememberSystemUiController(window: Window? = findWindow()): SystemUiController {
    val view = LocalView.current
    val isDarkMode = isSystemInDarkTheme()
    return remember(view, window) {
        SystemUiControllerImpl(
            view = view,
            isDarkMode = isDarkMode,
            window = window
        )
    }
}

@Composable
private fun findWindow(): Window? =
    (LocalView.current.parent as? DialogWindowProvider)?.window ?: LocalView.current.context.findWindow()

private tailrec fun Context.findWindow(): Window? =
    when (this) {
        is Activity -> window
        is ContextWrapper -> baseContext.findWindow()
        else -> null
    }
