package com.mindera.alfie.debug.view

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

interface DebugViewContent {

    fun content(navigator: DestinationsNavigator): @Composable () -> Unit
}
