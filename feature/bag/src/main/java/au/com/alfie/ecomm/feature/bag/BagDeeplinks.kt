package au.com.alfie.ecomm.feature.bag

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.core.deeplink.DeeplinkInterpreter
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkInstance
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkSpec
import au.com.alfie.ecomm.core.deeplink.model.deeplinkSpec
import au.com.alfie.ecomm.feature.bag.destinations.BagScreenDestination
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BagDeeplinks @Inject constructor() : DeeplinkGroup {

    companion object {
        private const val BAG_PATH_SEGMENT = "bag"
    }

    override val interpreters: List<DeeplinkInterpreter> = listOf(
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/bag
                deeplinkSpec {
                    appendFixedPathSegment(BAG_PATH_SEGMENT)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = DeeplinkResult.NavigateClearingStack(
                direction = BagScreenDestination,
                saveState = true,
                restoreState = true,
                launchSingleTop = true
            )
        }
    )
}
