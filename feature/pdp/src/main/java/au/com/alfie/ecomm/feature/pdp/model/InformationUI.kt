package au.com.alfie.ecomm.feature.pdp.model

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.designsystem.component.tab.TabItem

internal sealed class InformationUI(open val tabItem: TabItem) {

    @Stable
    data class Description(
        override val tabItem: TabItem,
        val content: String
    ) : InformationUI(tabItem)
}
