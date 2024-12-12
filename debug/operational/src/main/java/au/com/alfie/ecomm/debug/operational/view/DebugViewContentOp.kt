package au.com.alfie.ecomm.debug.operational.view

import androidx.compose.runtime.Composable
import au.com.alfie.ecomm.debug.operational.view.screen.DebugScreenOp
import au.com.alfie.ecomm.debug.view.DebugViewContent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import javax.inject.Inject

internal class DebugViewContentOp @Inject constructor() : DebugViewContent {

    override fun content(navigator: DestinationsNavigator): @Composable () -> Unit = {
        DebugScreenOp(navigator = navigator)
    }
}
