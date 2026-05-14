package com.mindera.alfie.debug.operational.view.featuretoggle

import android.content.Context
import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.debug.operational.R
import com.mindera.alfie.repository.featuretoggle.model.FeatureToggle
import com.mindera.alfie.repository.featuretoggle.model.FeatureToggleType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeatureToggleUIFactory @Inject constructor(
    private val dispatcher: DispatcherProvider,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke() = withContext(dispatcher.default()) {
        listOf(
            FeatureToggle(
                toggleTitle = context.getString(R.string.feature_toggle_show_wishlist),
                enabled = false,
                type = FeatureToggleType.SWITCH.value
            )
        )
    }
}
