package com.mindera.alfie.core.environment

import com.mindera.alfie.core.environment.model.Environment
import com.mindera.alfie.core.environment.model.Environments

interface EnvironmentManager {

    suspend fun current(): Environment

    fun environments(): Environments

    suspend fun custom(): Environment.Custom

    suspend fun update(environment: Environment)
}
