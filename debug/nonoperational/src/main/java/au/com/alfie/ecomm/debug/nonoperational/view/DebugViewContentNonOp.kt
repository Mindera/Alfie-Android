package au.com.alfie.ecomm.debug.nonoperational.view

import androidx.compose.runtime.Composable
import au.com.alfie.ecomm.debug.view.DebugViewContent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import javax.inject.Inject

internal class DebugViewContentNonOp @Inject constructor() : DebugViewContent {

    override fun content(navigator: DestinationsNavigator): @Composable () -> Unit = {}
}
