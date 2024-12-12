package au.com.alfie.ecomm.debug.operational.view.featuretoggle

import android.content.Context
import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.debug.operational.R
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggle
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggleType
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
