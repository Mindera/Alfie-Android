package au.com.alfie.ecomm.feature.account

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.core.deeplink.DeeplinkInterpreter
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkInstance
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkSpec
import au.com.alfie.ecomm.core.deeplink.model.deeplinkSpec
import au.com.alfie.ecomm.feature.account.destinations.AccountScreenDestination
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
