package com.mindera.alfie

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.mindera.alfie.core.commons.extension.isNotNullOrBlank
import com.mindera.alfie.core.deeplink.DeeplinkHandler
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.ui.system.rememberSystemUiController
import com.mindera.alfie.debug.runner.DebugComposeRunner
import com.mindera.alfie.debug.runner.LocalDebugComposeRunner
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.feature.startup.StartUp
import com.mindera.alfie.navigation.AppNavigation
import com.mindera.alfie.navigation.NavGraphs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var debugComposeRunner: DebugComposeRunner

    @Inject
    lateinit var navGraphs: NavGraphs

    @Inject
    lateinit var directionProvider: DirectionProvider

    @Inject
    lateinit var deeplinkHandler: DeeplinkHandler

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            Theme {
                val systemUiController = rememberSystemUiController()
                val wishlistToggle = viewModel.wishlistToggle.collectAsStateWithLifecycle()
                systemUiController.setSystemUiColors(
                    componentActivity = this,
                    statusBarLight = Theme.color.white,
                    statusBarDark = Theme.color.black,
                    navigationLight = Theme.color.black
                )

                CompositionLocalProvider(
                    LocalDebugComposeRunner provides debugComposeRunner
                ) {
                    StartUp { startDestination ->
                        AppNavigation(
                            startDestination = startDestination,
                            navGraphs = navGraphs,
                            directionProvider = directionProvider,
                            deeplinkHandler = deeplinkHandler,
                            wishlistToggleEnabled = wishlistToggle.value
                        )
                    }
                }
            }
        }

        getIntentUrl()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // This handling will be used when the app is already created, otherwise if the app is closed or has been
        // killed in the background, it will go through onCreate instead
        handleDeeplink(intent.dataString)
    }

    private fun getIntentUrl() {
        postOnMain {
            handleDeeplink(intent.dataString)

            // We need to manually clear the intent action and data so it isn't reused on follow up
            // app startups or configuration changes
            intent.action = ""
            intent.data = null
        }
    }

    private fun handleDeeplink(url: String?) {
        if (url.isNotNullOrBlank()) {
            lifecycleScope.launch {
                deeplinkHandler.handle(url)
            }
        }
    }

    private fun postOnMain(action: () -> Unit) {
        // Since deep link can be performed very early in the lifecycle we need to postpone
        // it slightly so the UI has enough time to be created
        Handler(Looper.getMainLooper()).post {
            action()
        }
    }
}
