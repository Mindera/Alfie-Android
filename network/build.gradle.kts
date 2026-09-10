import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.BuildType
import com.mindera.alfie.buildconvention.module.ProjectModule

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
    testImplementation(libs.apollo.testing)

    implementation(project(ProjectModule.debug))
    implementation(project(ProjectModule.coreEnvironment))
}

apollo {
    // The only service. The legacy server and its Apollo service were removed once the last
    // consumer (brands) went; queries live under src/main/graphql/bff/.
    service("bff") {
        packageName.set("${AppConfig.applicationId}.graphql.bff")
        schemaFiles.from(file("src/main/graphql/schema-new.graphqls"))
        srcDir(file("src/main/graphql/bff"))
        introspection {
            endpointUrl.set("http://10.0.2.2:4000/graphql")
            schemaFile.set(file("src/main/graphql/schema-new.graphqls"))
        }
    }
}
