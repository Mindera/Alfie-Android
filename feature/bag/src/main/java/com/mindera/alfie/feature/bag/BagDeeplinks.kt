package com.mindera.alfie.feature.bag

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.core.deeplink.DeeplinkInterpreter
import com.mindera.alfie.core.deeplink.DeeplinkResult
import com.mindera.alfie.core.deeplink.model.DeeplinkInstance
import com.mindera.alfie.core.deeplink.model.DeeplinkSpec
import com.mindera.alfie.core.deeplink.model.deeplinkSpec
import com.mindera.alfie.feature.bag.destinations.BagScreenDestination
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
