package au.com.alfie.ecomm.debug.operational.runner

import au.com.alfie.ecomm.debug.runner.DebugSuspendRunner
import javax.inject.Inject

internal class DebugSuspendRunnerOp @Inject constructor() : DebugSuspendRunner {

    override suspend fun invoke(block: suspend () -> Unit) = block()

    override suspend fun <T> invoke(
        onDebug: suspend () -> T,
        onRelease: suspend () -> T
    ): T = onDebug()
}
