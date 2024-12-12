package au.com.alfie.ecomm.feature.startup.loader

internal interface StartUpLoader {

    suspend fun load(): Result<Unit>
}
