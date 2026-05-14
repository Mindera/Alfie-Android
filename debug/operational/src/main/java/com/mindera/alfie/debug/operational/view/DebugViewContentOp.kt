package com.mindera.alfie.debug.operational.view

import androidx.compose.runtime.Composable
import com.mindera.alfie.debug.operational.view.screen.DebugScreenOp
import com.mindera.alfie.debug.view.DebugViewContent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import javax.inject.Inject

internal class DebugViewContentOp @Inject constructor() : DebugViewContent {

    override fun content(navigator: DestinationsNavigator): @Composable () -> Unit = {
        DebugScreenOp(navigator = navigator)
    }
}
