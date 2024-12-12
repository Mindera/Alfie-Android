package au.com.alfie.ecomm.core.test.debug

import au.com.alfie.ecomm.debug.runner.DebugRunner

class TestDebugRunner(var isRelease: Boolean) : DebugRunner {

    override fun invoke(block: () -> Unit) {
        if (isRelease.not()) block()
    }

    override fun <T> invoke(onDebug: () -> T, onRelease: () -> T): T {
        return if (isRelease) onRelease() else onDebug()
    }
}
