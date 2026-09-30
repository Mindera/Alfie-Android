package com.mindera.alfie.designsystem.component.topbar.custom

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import com.mindera.alfie.designsystem.R

@Stable
sealed interface LandingHeaderType {

    /**
     * Left over from the pre-rollout header. The modern Home design (Figma node `672:80414`) has
     * no logged-in variant — it is the brand wordmark in both states — so [LandingHeader] renders
     * this centred in the same container as [Logo] purely so the branch stays usable from the
     * debug catalog. Whether a signed-in Home greets the user is still an open design question.
     */
    data class Greeting(
        val userName: String,
        val subtitle: String? = null
    ) : LandingHeaderType

    /** The stacked MINDERA / ALFIE wordmark — Figma `doc_branding`, intrinsic 160x49. */
    data class Logo(
        @DrawableRes
        val icon: Int = R.drawable.brand_logo,
        val contentDescription: String? = null
    ) : LandingHeaderType
}
