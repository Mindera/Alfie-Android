package au.com.alfie.ecomm.feature.startup.loader

import kotlinx.coroutines.delay
import javax.inject.Inject

internal class SampleStartUpLoader @Inject constructor() : StartUpLoader {

    override suspend fun load(): Result<Unit> {
        // Keeping for demonstration purposes
        // TODO Remove when adding the first loader
        delay(timeMillis = 1500L)

        return Result.success(Unit)
    }
}
