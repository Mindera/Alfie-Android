package au.com.alfie.ecomm.core.environment

import au.com.alfie.ecomm.core.environment.model.BuildConfiguration
import au.com.alfie.ecomm.core.environment.model.Environment
import au.com.alfie.ecomm.core.environment.model.Environments
import au.com.alfie.ecomm.data.datastore.DebugPreferencesProto.EnvironmentProto
import au.com.alfie.ecomm.data.datastore.debug.DebugPreferencesDataSource
import au.com.alfie.ecomm.debug.runner.DebugSuspendRunner
import javax.inject.Inject

private const val DEFAULT_CUSTOM_URL = "https://api-preview-000.localhost:4000/graphql"
private const val DEFAULT_WEB_URL = "https://www.alfie.com"

internal class EnvironmentManagerImpl @Inject constructor(
    private val debugPreferencesDataSource: DebugPreferencesDataSource,
    private val buildConfiguration: BuildConfiguration,
    private val debugRunner: DebugSuspendRunner
) : EnvironmentManager {

    override suspend fun current(): Environment = debugRunner(
        onRelease = { buildConfiguration.environments.prod },
        onDebug = {
            debugPreferencesDataSource.getEnvironment { environment, customUrl ->
                when (environment) {
                    EnvironmentProto.DEV,
                    EnvironmentProto.UNRECOGNIZED -> buildConfiguration.environments.dev
                    EnvironmentProto.PRE_PROD -> buildConfiguration.environments.preProd
                    EnvironmentProto.PROD -> buildConfiguration.environments.prod
                    EnvironmentProto.CUSTOM -> Environment.Custom(graphQLUrl = customUrl.orEmpty(), webUrl = DEFAULT_WEB_URL)
                }
            }
        }
    )

    override fun environments(): Environments = buildConfiguration.environments

    override suspend fun custom(): Environment.Custom = current() as? Environment.Custom
        ?: Environment.Custom(
            graphQLUrl = debugPreferencesDataSource.getCustomUrl()
                ?.takeIf { it.isNotBlank() }
                ?: DEFAULT_CUSTOM_URL,
            webUrl = DEFAULT_WEB_URL
        )

    override suspend fun update(environment: Environment) {
        debugPreferencesDataSource.updateEnvironment {
            when (environment) {
                is Environment.Dev -> EnvironmentProto.DEV
                is Environment.PreProd -> EnvironmentProto.PRE_PROD
                is Environment.Prod -> EnvironmentProto.PROD
                is Environment.Custom -> EnvironmentProto.CUSTOM
            }
        }
        if (environment is Environment.Custom) {
            debugPreferencesDataSource.updateCustomUrl(environment.graphQLUrl)
        }
    }
}
