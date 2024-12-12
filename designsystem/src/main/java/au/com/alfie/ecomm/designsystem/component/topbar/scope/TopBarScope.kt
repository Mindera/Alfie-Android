package au.com.alfie.ecomm.designsystem.component.topbar.scope

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Immutable
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState

@Immutable
interface TopBarScope {

    val state: TopBarState

    @OptIn(ExperimentalMaterial3Api::class)
    val topBarColors: TopAppBarColors
}

@Immutable
@OptIn(ExperimentalMaterial3Api::class)
internal class TopBarScopeInstance(
    override val state: TopBarState,
    override val topBarColors: TopAppBarColors
) : TopBarScope
