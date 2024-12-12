package au.com.alfie.ecomm.debug.operational.view.environment.model

import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.environment.model.Environment

internal data class EnvironmentUI(
    val environment: Environment,
    val isEnabled: Boolean,
    val isSelected: Boolean,
    val enableUrlChange: Boolean,
    val label: StringResource
)
