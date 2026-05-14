package com.mindera.alfie.feature.shop.category.model

internal sealed interface CategoryEvent {

    data class OnEntryClickEvent(val entry: CategoryEntryUI) : CategoryEvent
}
