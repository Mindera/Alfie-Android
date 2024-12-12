package au.com.alfie.ecomm.feature.shop.delegate

import androidx.lifecycle.ViewModel
import au.com.alfie.ecomm.feature.shop.brand.model.BrandEntryUI
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEntryUI

internal interface NavigateToEntry {

    fun ViewModel.openCategoryEntry(entry: CategoryEntryUI)

    fun ViewModel.openBrandEntry(entry: BrandEntryUI.Entry)
}
