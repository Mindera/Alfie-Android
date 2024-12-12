package au.com.alfie.ecomm.debug.nonoperational.runner

import au.com.alfie.ecomm.debug.runner.DebugRunner
import javax.inject.Inject

internal class DebugRunnerNonOp @Inject constructor() : DebugRunner {

    override fun invoke(block: () -> Unit) = Unit

    override fun <T> invoke(
        onDebug: () -> T,
        onRelease: () -> T
    ): T = onRelease()
}
