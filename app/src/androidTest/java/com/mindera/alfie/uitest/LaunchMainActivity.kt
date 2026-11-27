package com.mindera.alfie.uitest

import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.mindera.alfie.MainActivity

/**
 * The one way UI journeys start the app: [MainActivity] through [ActivityScenario], from the
 * launcher, or with [deepLink] through the app's real deep-link path (`MainActivity` →
 * `DeeplinkHandler`). The explicit intent skips the manifest's host matching, which `DeeplinkSpec`
 * ignores anyway.
 *
 * Use it with `createEmptyComposeRule()`, and close the scenario when the test ends:
 * `launchMainActivity().use { ... }`.
 */
fun launchMainActivity(deepLink: String? = null): ActivityScenario<MainActivity> {
    val intent = if (deepLink == null) {
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
    } else {
        Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
    }
    intent.setClass(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
    return ActivityScenario.launch(intent)
}
