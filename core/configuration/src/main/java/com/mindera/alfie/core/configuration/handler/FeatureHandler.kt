package com.mindera.alfie.core.configuration.handler

import com.mindera.alfie.core.configuration.FeatureKey

interface FeatureHandler {

    suspend fun isEnabled(key: FeatureKey): Boolean
}
