package com.mindera.alfie.feature.pdp.model

import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.feature.uievent.UIEvent

internal data class ShareEvent(
    val title: String,
    val content: StringResource
) : UIEvent.Custom
