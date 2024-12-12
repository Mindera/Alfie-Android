package au.com.alfie.ecomm.debug.operational.runner

import androidx.compose.runtime.Composable
import au.com.alfie.ecomm.debug.runner.DebugComposeRunner
import javax.inject.Inject

internal class DebugComposeRunnerOp @Inject constructor() : DebugComposeRunner {

    @Composable
    override fun invoke(block: @Composable () -> Unit) = block()
}
