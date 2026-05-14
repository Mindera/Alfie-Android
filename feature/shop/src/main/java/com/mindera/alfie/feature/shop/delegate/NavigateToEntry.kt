package com.mindera.alfie.feature.shop.delegate

import androidx.lifecycle.ViewModel
import com.mindera.alfie.feature.shop.brand.model.BrandEntryUI
import com.mindera.alfie.feature.shop.category.model.CategoryEntryUI

internal interface NavigateToEntry {

    fun ViewModel.openCategoryEntry(entry: CategoryEntryUI)

    fun ViewModel.openBrandEntry(entry: BrandEntryUI.Entry)
}
