package com.mindera.alfie.debug.nonoperational.runner

import com.mindera.alfie.debug.runner.DebugSuspendRunner
import javax.inject.Inject

internal class DebugSuspendRunnerNonOp @Inject constructor() : DebugSuspendRunner {

    override suspend fun invoke(block: suspend () -> Unit) = Unit

    override suspend fun <T> invoke(
        onDebug: suspend () -> T,
        onRelease: suspend () -> T
    ): T = onRelease()
}
