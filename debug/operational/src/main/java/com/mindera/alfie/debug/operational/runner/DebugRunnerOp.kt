package com.mindera.alfie.debug.operational.runner

import com.mindera.alfie.debug.runner.DebugRunner
import javax.inject.Inject

internal class DebugRunnerOp @Inject constructor() : DebugRunner {

    override fun invoke(block: () -> Unit) = block()

    override fun <T> invoke(
        onDebug: () -> T,
        onRelease: () -> T
    ): T = onDebug()
}
