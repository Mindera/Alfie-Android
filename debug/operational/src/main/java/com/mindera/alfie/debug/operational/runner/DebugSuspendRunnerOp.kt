package com.mindera.alfie.debug.operational.runner

import com.mindera.alfie.debug.runner.DebugSuspendRunner
import javax.inject.Inject

internal class DebugSuspendRunnerOp @Inject constructor() : DebugSuspendRunner {

    override suspend fun invoke(block: suspend () -> Unit) = block()

    override suspend fun <T> invoke(
        onDebug: suspend () -> T,
        onRelease: suspend () -> T
    ): T = onDebug()
}
