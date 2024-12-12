package au.com.alfie.ecomm.debug.runner

interface DebugRunner {

    operator fun invoke(block: () -> Unit)

    operator fun <T> invoke(
        onDebug: () -> T,
        onRelease: () -> T
    ): T
}
