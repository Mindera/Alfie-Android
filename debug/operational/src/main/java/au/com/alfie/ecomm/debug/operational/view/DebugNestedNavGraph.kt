package au.com.alfie.ecomm.debug.operational.view

import au.com.alfie.ecomm.core.navigation.NestedNavGraph
import com.ramcosta.composedestinations.spec.NavGraphSpec
import javax.inject.Inject

private const val ROUTE = "debug"

internal class DebugNestedNavGraph @Inject constructor() : NestedNavGraph {

    override val navGraphSpec: NavGraphSpec = NavGraphs.root.copy(route = ROUTE)
}
