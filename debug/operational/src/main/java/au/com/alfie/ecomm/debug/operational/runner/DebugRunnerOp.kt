package au.com.alfie.ecomm.debug.operational.runner

import au.com.alfie.ecomm.debug.runner.DebugRunner
import javax.inject.Inject

internal class DebugRunnerOp @Inject constructor() : DebugRunner {

    override fun invoke(block: () -> Unit) = block()

    override fun <T> invoke(
        onDebug: () -> T,
        onRelease: () -> T
    ): T = onDebug()
}
