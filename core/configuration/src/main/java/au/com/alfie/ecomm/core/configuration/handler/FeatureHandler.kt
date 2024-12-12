package au.com.alfie.ecomm.core.configuration.handler

import au.com.alfie.ecomm.core.configuration.FeatureKey

interface FeatureHandler {

    suspend fun isEnabled(key: FeatureKey): Boolean
}
