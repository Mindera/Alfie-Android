package com.mindera.alfie.feature.pdp.model

import androidx.compose.runtime.Stable
import com.mindera.alfie.designsystem.component.tab.TabItem

internal sealed class InformationUI(open val tabItem: TabItem) {

    @Stable
    data class Description(
        override val tabItem: TabItem,
        val content: String
    ) : InformationUI(tabItem)
}
