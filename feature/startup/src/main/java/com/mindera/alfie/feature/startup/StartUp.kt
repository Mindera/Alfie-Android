package com.mindera.alfie.feature.startup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.designsystem.component.loading.LogoLoading
import com.mindera.alfie.designsystem.theme.Theme
@Composable
fun StartUp(
    appContent: @Composable (startDestination: Screen) -> Unit
) {
    val viewModel: StartUpViewModel = hiltViewModel()
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()

    if (startDestination == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.white),
            contentAlignment = Alignment.Center
        ) {
            LogoLoading()
        }
    } else {
        appContent(startDestination ?: Screen.Home)
    }
}
