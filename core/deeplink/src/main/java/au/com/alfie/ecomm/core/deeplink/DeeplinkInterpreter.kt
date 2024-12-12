package au.com.alfie.ecomm.core.deeplink

import au.com.alfie.ecomm.core.deeplink.model.DeeplinkInstance
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkSpec

interface DeeplinkInterpreter {

    val specs: List<DeeplinkSpec>

    suspend fun handle(instance: DeeplinkInstance): DeeplinkResult
}
