package au.com.alfie.ecomm.buildconvention.module

object ProjectModule {

    const val coreAnalytics = ":core:analytics"
    const val coreCommons = ":core:commons"
    const val coreConfiguration = ":core:configuration"
    const val coreDeeplink = ":core:deeplink"
    const val coreEnvironment = ":core:environment"
    const val coreNavigation = ":core:navigation"
    const val coreSync = ":core:sync"
    const val coreTest = ":core:test"
    const val coreUi = ":core:ui"
    const val data = ":data"
    const val dataDatabase = ":data:database"
    const val dataDatastore = ":data:datastore"
    const val debug = ":debug"
    const val debugOperational = ":debug:operational"
    const val debugNonOperational = ":debug:nonoperational"
    const val designSystem = ":designsystem"
    const val domain = ":domain"
    const val feature = ":feature"
    const val repository = ":domain:repository"
    const val network = ":network"

    val modules = listOf(
        coreAnalytics,
        coreCommons,
        coreConfiguration,
        coreDeeplink,
        coreEnvironment,
        coreNavigation,
        coreSync,
        coreUi,
        data,
        dataDatabase,
        dataDatastore,
        designSystem,
        domain,
        feature,
        network,
        repository
    )
}
