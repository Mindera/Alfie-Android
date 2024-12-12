package au.com.alfie.ecomm.core.environment

import au.com.alfie.ecomm.core.environment.model.Environment
import au.com.alfie.ecomm.core.environment.model.Environments

interface EnvironmentManager {

    suspend fun current(): Environment

    fun environments(): Environments

    suspend fun custom(): Environment.Custom

    suspend fun update(environment: Environment)
}
