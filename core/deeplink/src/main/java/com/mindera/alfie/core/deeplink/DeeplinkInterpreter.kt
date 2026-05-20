package com.mindera.alfie.core.deeplink

import com.mindera.alfie.core.deeplink.model.DeeplinkInstance
import com.mindera.alfie.core.deeplink.model.DeeplinkSpec

interface DeeplinkInterpreter {

    val specs: List<DeeplinkSpec>

    suspend fun handle(instance: DeeplinkInstance): DeeplinkResult
}
