package au.com.alfie.ecomm.debug.nonoperational.runner

import androidx.compose.runtime.Composable
import au.com.alfie.ecomm.debug.runner.DebugComposeRunner
import javax.inject.Inject

internal class DebugComposeRunnerNonOp @Inject constructor() : DebugComposeRunner {

    @Composable
    override fun invoke(block: @Composable () -> Unit) = Unit
}
