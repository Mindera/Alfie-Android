package com.mindera.alfie.debug.operational.view

import com.mindera.alfie.core.navigation.NestedNavGraph
import com.ramcosta.composedestinations.spec.NavGraphSpec
import javax.inject.Inject

private const val ROUTE = "debug"

internal class DebugNestedNavGraph @Inject constructor() : NestedNavGraph {

    override val navGraphSpec: NavGraphSpec = NavGraphs.root.copy(route = ROUTE)
}
