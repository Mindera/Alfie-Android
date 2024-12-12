pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("buildConvention") {
            from(files("gradle/buildConvention.versions.toml"))
        }
    }
}

rootProject.name = "Alfie"
includeBuild("buildconvention")
include(":app")
include(":core")
include(":core:analytics")
include(":core:commons")
include(":core:configuration")
include(":core:deeplink")
include(":core:environment")
include(":core:sync")
include(":core:test")
include(":core:ui")
include(":core:navigation")
include(":data")
include(":data:database")
include(":data:datastore")
include(":debug")
include(":debug:nonoperational")
include(":debug:operational")
include(":designsystem")
include(":domain")
include(":domain:repository")
include(":feature")
include(":feature:account")
include(":feature:bag")
include(":feature:debug")
include(":feature:home")
include(":feature:pdp")
include(":feature:plp")
include(":feature:search")
include(":feature:shop")
include(":feature:startup")
include(":feature:webview")
include(":feature:wishlist")
include(":network")
