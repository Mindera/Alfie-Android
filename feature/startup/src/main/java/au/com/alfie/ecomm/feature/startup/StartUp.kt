package au.com.alfie.ecomm.feature.startup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.designsystem.component.loading.LogoLoading
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
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            LogoLoading()
        }
    } else {
        appContent(startDestination ?: Screen.Home)
    }
}
