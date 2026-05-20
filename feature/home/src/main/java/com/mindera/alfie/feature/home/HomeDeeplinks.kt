package com.mindera.alfie.feature.home

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.core.deeplink.DeeplinkInterpreter
import com.mindera.alfie.core.deeplink.DeeplinkResult
import com.mindera.alfie.core.deeplink.model.DeeplinkInstance
import com.mindera.alfie.core.deeplink.model.DeeplinkSpec
import com.mindera.alfie.core.deeplink.model.deeplinkSpec
import com.mindera.alfie.feature.home.destinations.HomeScreenDestination
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HomeDeeplinks @Inject constructor() : DeeplinkGroup {

    override val interpreters: List<DeeplinkInterpreter> = listOf(
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/
                deeplinkSpec { } // Same as DeeplinkSpec(pathSegments = emptyList(), queryParameters = emptyList())
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = DeeplinkResult.NavigateTo(
                direction = HomeScreenDestination,
                navOptions = {
                    launchSingleTop = true
                    popUpTo(HomeScreenDestination.route) {
                        inclusive = false
                    }
                }
            )
        }
    )
}
