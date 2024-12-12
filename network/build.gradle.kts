import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.BuildType
import au.com.alfie.ecomm.buildconvention.Environment
import au.com.alfie.ecomm.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.apollo)
}

android {
    namespace = AppConfig.applicationId + ".network"

    buildTypes {
        debug {
            matchingFallbacks.add(BuildType.DEBUG.buildName)
        }

        create(BuildType.BETA.buildName) {
            matchingFallbacks.add(BuildType.RELEASE.buildName)
            matchingFallbacks.add(BuildType.DEBUG.buildName)
        }

        release {
            matchingFallbacks.add(BuildType.RELEASE.buildName)
        }
    }
}

dependencies {
    api(libs.apollo)
    testApi(libs.apollo.testing)

    implementation(project(ProjectModule.debug))
    implementation(project(ProjectModule.coreEnvironment))
}

apollo {
    val schemaPath = "src/main/graphql/schema.graphqls"
    Environment.values().forEach { environment ->
        service(environment.name) {
            packageName.set("${AppConfig.applicationId}.graphql")
            introspection {
                endpointUrl.set(environment.url)
                schemaFile.set(file(schemaPath))
            }
            outputDirConnection {
                connectToAndroidSourceSet(environment.buildType.buildName)
            }
        }
    }
}
