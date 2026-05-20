package com.mindera.alfie.debug.nonoperational.runner

import com.mindera.alfie.debug.runner.DebugRunner
import javax.inject.Inject

internal class DebugRunnerNonOp @Inject constructor() : DebugRunner {

    override fun invoke(block: () -> Unit) = Unit

    override fun <T> invoke(
        onDebug: () -> T,
        onRelease: () -> T
    ): T = onRelease()
}
