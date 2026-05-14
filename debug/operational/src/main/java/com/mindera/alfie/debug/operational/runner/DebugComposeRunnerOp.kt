package com.mindera.alfie.debug.operational.runner

import androidx.compose.runtime.Composable
import com.mindera.alfie.debug.runner.DebugComposeRunner
import javax.inject.Inject

internal class DebugComposeRunnerOp @Inject constructor() : DebugComposeRunner {

    @Composable
    override fun invoke(block: @Composable () -> Unit) = block()
}
