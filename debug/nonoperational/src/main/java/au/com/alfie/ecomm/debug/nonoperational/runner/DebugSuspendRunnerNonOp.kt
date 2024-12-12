package au.com.alfie.ecomm.debug.nonoperational.runner

import au.com.alfie.ecomm.debug.runner.DebugSuspendRunner
import javax.inject.Inject

internal class DebugSuspendRunnerNonOp @Inject constructor() : DebugSuspendRunner {

    override suspend fun invoke(block: suspend () -> Unit) = Unit

    override suspend fun <T> invoke(
        onDebug: suspend () -> T,
        onRelease: suspend () -> T
    ): T = onRelease()
}
