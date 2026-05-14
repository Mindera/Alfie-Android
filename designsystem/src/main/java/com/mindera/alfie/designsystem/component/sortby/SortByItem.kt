package com.mindera.alfie.designsystem.component.sortby

import androidx.annotation.DrawableRes

data class SortByItem(
    val text: String,
    @DrawableRes val icon: Int? = null
)
