package au.com.alfie.ecomm.feature.shop.category.model

internal sealed interface CategoryEvent {

    data class OnEntryClickEvent(val entry: CategoryEntryUI) : CategoryEvent
}
