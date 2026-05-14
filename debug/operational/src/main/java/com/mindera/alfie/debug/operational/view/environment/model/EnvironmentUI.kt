package com.mindera.alfie.debug.operational.view.environment.model

import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.environment.model.Environment

internal data class EnvironmentUI(
    val environment: Environment,
    val isEnabled: Boolean,
    val isSelected: Boolean,
    val enableUrlChange: Boolean,
    val label: StringResource
)
