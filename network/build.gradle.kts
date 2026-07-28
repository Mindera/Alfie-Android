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
    testApi(libs.apollo.testing)

    implementation(project(ProjectModule.debug))
    implementation(project(ProjectModule.coreEnvironment))
}

apollo {
    // Legacy service — queries against the old server (port 4000).
    // Migrate features to the "new" service below one by one.
    //
    // WARNING: the BFF now serves on 4000 too, so both introspection tasks point at the same
    // endpoint. Do not run downloadLegacyApolloSchemaFromIntrospection while only the BFF is
    // running — it would overwrite schema-legacy.graphqls with the BFF schema.
    service("legacy") {
        packageName.set("${AppConfig.applicationId}.graphql")
        schemaFiles.from(file("src/main/graphql/schema-legacy.graphqls"))
        srcDir(file("src/main/graphql/legacy"))
        introspection {
            endpointUrl.set("http://10.0.2.2:4000/graphql")
            schemaFile.set(file("src/main/graphql/schema-legacy.graphqls"))
        }
    }

    // New BFF service — queries against the new server (port 4000).
    // Add migrated .graphql files under src/main/graphql/bff/.
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
