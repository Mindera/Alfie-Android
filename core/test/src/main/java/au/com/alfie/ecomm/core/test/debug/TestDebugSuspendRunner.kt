package au.com.alfie.ecomm.core.test.debug

import au.com.alfie.ecomm.debug.runner.DebugSuspendRunner

class TestDebugSuspendRunner(var isRelease: Boolean) : DebugSuspendRunner {

    override suspend fun invoke(block: suspend () -> Unit) {
        if (isRelease.not()) block()
    }

    override suspend fun <T> invoke(onDebug: suspend () -> T, onRelease: suspend () -> T): T {
        return if (isRelease) onRelease() else onDebug()
    }
}
