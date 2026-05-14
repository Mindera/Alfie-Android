package com.mindera.alfie.debug.nonoperational.view

import androidx.compose.runtime.Composable
import com.mindera.alfie.debug.view.DebugViewContent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import javax.inject.Inject

internal class DebugViewContentNonOp @Inject constructor() : DebugViewContent {

    override fun content(navigator: DestinationsNavigator): @Composable () -> Unit = {}
}
