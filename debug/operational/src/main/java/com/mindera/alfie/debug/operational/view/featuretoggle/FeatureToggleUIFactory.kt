package com.mindera.alfie.debug.operational.view.featuretoggle

import com.mindera.alfie.repository.featuretoggle.model.FeatureToggle
import javax.inject.Inject

/**
 * Registry of debug feature toggles seeded into the local store on first open of the
 * Feature Toggle screen.
 *
 * Currently empty: the only entry was "Show Wishlist On Bottom Bar", removed in ALFMOB-448
 * because the Figma design shows the Wishlist tab unconditionally. Add new toggles here.
 */
class FeatureToggleUIFactory @Inject constructor() {
    operator fun invoke(): List<FeatureToggle> = emptyList()
}
