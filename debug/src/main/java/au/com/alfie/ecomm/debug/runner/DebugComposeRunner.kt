package au.com.alfie.ecomm.debug.runner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf

@Stable
interface DebugComposeRunner {

    @Composable
    operator fun invoke(block: @Composable () -> Unit)
}

internal val defaultDebugComposeRunner = object : DebugComposeRunner {
    @Composable
    override fun invoke(block: @Composable () -> Unit) = Unit
}

val LocalDebugComposeRunner = staticCompositionLocalOf { defaultDebugComposeRunner }
