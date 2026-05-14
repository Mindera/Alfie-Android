package com.mindera.alfie.feature.account

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.core.deeplink.DeeplinkInterpreter
import com.mindera.alfie.core.deeplink.DeeplinkResult
import com.mindera.alfie.core.deeplink.model.DeeplinkInstance
import com.mindera.alfie.core.deeplink.model.DeeplinkSpec
import com.mindera.alfie.core.deeplink.model.deeplinkSpec
import com.mindera.alfie.feature.account.destinations.AccountScreenDestination
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AccountDeeplinks @Inject constructor() : DeeplinkGroup {

    companion object {
        private const val ACCOUNT_PATH_SEGMENT = "account"
    }

    override val interpreters: List<DeeplinkInterpreter> = listOf(
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/account
                deeplinkSpec {
                    appendFixedPathSegment(ACCOUNT_PATH_SEGMENT)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult =
                DeeplinkResult.NavigateTo(direction = AccountScreenDestination)
        }
    )
}
