package au.com.alfie.ecomm.debug.runner

interface DebugSuspendRunner {

    suspend operator fun invoke(block: suspend () -> Unit)

    suspend operator fun <T> invoke(
        onDebug: suspend () -> T,
        onRelease: suspend () -> T
    ): T
}
