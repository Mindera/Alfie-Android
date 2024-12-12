package au.com.alfie.ecomm.feature.home

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.core.deeplink.DeeplinkInterpreter
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkInstance
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkSpec
import au.com.alfie.ecomm.core.deeplink.model.deeplinkSpec
import au.com.alfie.ecomm.feature.home.destinations.HomeScreenDestination
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
