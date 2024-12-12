package au.com.alfie.ecomm.core.commons.log

import au.com.alfie.ecomm.debug.runner.DebugRunner
import timber.log.Timber
import javax.inject.Inject

class TimberConfigurator @Inject constructor(
    private val debugRunner: DebugRunner
) {

    fun setup() {
        debugRunner(
            onDebug = { Timber.plant(Timber.DebugTree()) },
            onRelease = { Timber.plant(ReleaseTree()) }
        )
    }
}
