package com.mindera.alfie.feature.startup.loader

internal interface StartUpLoader {

    suspend fun load(): Result<Unit>
}
