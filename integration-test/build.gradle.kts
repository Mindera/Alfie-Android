import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.BuildType
import com.mindera.alfie.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
}

android {
    namespace = AppConfig.applicationId + ".integrationtest"

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
    // Instrumented integration tests only — exercises the generated BFF Apollo operations
    // (com.mindera.alfie.graphql.bff.*) against a locally-running BFF. Apollo runtime + OkHttp
    // come transitively from :network's `api(libs.apollo)`.
    androidTestImplementation(project(ProjectModule.network))
    androidTestImplementation(libs.apollo)
    androidTestImplementation(libs.test.runner)
}
