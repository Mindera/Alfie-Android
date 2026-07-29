package com.mindera.alfie.feature.startup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.component.loading.LoadingSpinner
import com.mindera.alfie.designsystem.component.loading.LoadingSpinnerSize
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

@Composable
fun StartUp(
    appContent: @Composable (startDestination: Screen) -> Unit
) {
    val viewModel: StartUpViewModel = hiltViewModel()
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
    val theme = LocalTheme.current

    if (startDestination == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                // D1: background = Figma surface/background-primary (#ffffff)
                .background(theme.color.surface.backgroundPrimary),
            // D6: content centred both axes
            contentAlignment = Alignment.Center
        ) {
            SplashContent()
        }
    } else {
        appContent(startDestination ?: Screen.Home)
    }
}

@Composable
private fun SplashContent(
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        // D5: wordmark -> spinner gap = Figma spacing/spacing-s (16)
        verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing16)
    ) {
        // D2: MINDERA / ALFIE wordmark (vector, intrinsic 160x49), colour tokenised via content-primary
        Icon(
            painter = painterResource(R.drawable.brand_logo),
            contentDescription = null,
            tint = theme.color.content.contentPrimary
        )
        // D3: design-system LoadingSpinner, Small (24dp) — D4: colour defaults to content/content-primary
        LoadingSpinner(size = LoadingSpinnerSize.Small)
    }
}

@Preview
@Composable
private fun SplashContentPreview() {
    Theme {
        SplashContent()
    }
}
