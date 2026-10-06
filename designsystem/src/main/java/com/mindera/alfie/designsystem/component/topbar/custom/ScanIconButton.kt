package com.mindera.alfie.designsystem.component.topbar.custom

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.test.HOME_SCAN_BUTTON
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.animation.DefaultVisibilityAnimation
import com.mindera.alfie.designsystem.animation.defaultFadeIn
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme

/**
 * Barcode-scanner affordance that trails the search field in [SearchHeader].
 *
 * Deliberately built to the same geometry as [com.mindera.alfie.designsystem.component.topbar
 * .component.DefaultNavigationIcon]: a 40dp slot around a 32dp button around a 24dp glyph. The
 * two sit at opposite ends of the same band, and the 40dp slot is what holds the band at 48dp in
 * both search states — so they must not drift apart.
 *
 * The icon itself is a placeholder pending design; only `ic_scan.xml` changes when it lands.
 */
@Composable
internal fun ScanIconButton(
    onClick: ClickEvent,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    enterTransition: EnterTransition = defaultFadeIn(),
    exitTransition: ExitTransition = ExitTransition.None
) {
    DefaultVisibilityAnimation(
        isVisible = isVisible,
        enterTransition = enterTransition,
        exitTransition = exitTransition
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                modifier = Modifier
                    .size(Theme.iconSize.large)
                    .testTag(HOME_SCAN_BUTTON),
                onClick = onClick
            ) {
                Icon(
                    modifier = Modifier.size(Theme.iconSize.medium),
                    painter = painterResource(id = AlfieIcons.Scan),
                    contentDescription = stringResource(id = R.string.scan_content_description)
                )
            }
        }
    }
}
