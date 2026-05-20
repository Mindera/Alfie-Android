package com.mindera.alfie.core.commons.log

import com.mindera.alfie.debug.runner.DebugRunner
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
