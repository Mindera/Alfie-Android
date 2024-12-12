package au.com.alfie.ecomm.feature.pdp.model

import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.feature.uievent.UIEvent

internal data class ShareEvent(
    val title: String,
    val content: StringResource
) : UIEvent.Custom
