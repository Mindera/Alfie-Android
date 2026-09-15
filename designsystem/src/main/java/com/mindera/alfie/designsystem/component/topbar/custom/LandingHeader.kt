package com.mindera.alfie.designsystem.component.topbar.custom

import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.core.ui.test.HOME_TITLE_HEADER
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.animation.DefaultVisibilityAnimation
import com.mindera.alfie.designsystem.animation.standardAccelerate
import com.mindera.alfie.designsystem.component.searchbar.rememberSearchState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.component.topbar.action.TopBarActions
import com.mindera.alfie.designsystem.component.topbar.custom.LandingHeaderType.Greeting
import com.mindera.alfie.designsystem.component.topbar.custom.LandingHeaderType.Logo
import com.mindera.alfie.designsystem.component.topbar.scope.TopBarScope
import com.mindera.alfie.designsystem.component.topbar.scope.TopBarScopeInstance
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

/**
 * The level-1 landing header — Figma Home (node `672:80414`, frames `172:57031` / `262:40635`).
 *
 * Idle it is a 145dp block: `screen-size/margin` (16) of padding around a centred column of the
 * 160x49 brand wordmark, a `spacing/spacing-lg` (24) gap, and the 40dp search field. No divider —
 * the content below starts flush against it.
 *
 * Opening search collapses the branding away and tightens [SearchHeader] to its 48dp band, so the
 * header becomes the Search page header and matches the one Shop shows.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScope.LandingHeader(
    type: LandingHeaderType,
    modifier: Modifier = Modifier
) {
    val searchState = state.getSearchState() ?: return
    val theme = LocalTheme.current

    // `containerColor` resolves to neutrals0 — the same colour as surface/background-primary in
    // Figma — and keeps the scope's isDarkTheme handling and parity with SearchHeader.
    Column(modifier = modifier.background(topBarColors.containerColor)) {
        LandingHeaderBranding(type = type, isVisible = !searchState.isSearchOpen)
        SearchHeader(
            searchState = searchState,
            // The column gap and the frame's bottom padding, which the band owns while idle.
            idleTopPadding = theme.spacing.spacing24,
            idleBottomPadding = theme.spacing.spacing16
        )
    }
}

/**
 * The branding row above the field. Its top padding lives inside the animation so that it
 * collapses together with the wordmark — search mode then lands on exactly the 48dp band.
 */
@Composable
private fun TopBarScope.LandingHeaderBranding(
    type: LandingHeaderType,
    isVisible: Boolean
) {
    val theme = LocalTheme.current
    DefaultVisibilityAnimation(
        isVisible = isVisible,
        enterTransition = fadeIn(standardAccelerate()) + expandVertically(standardAccelerate()),
        exitTransition = shrinkVertically(standardAccelerate()) + fadeOut(standardAccelerate())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = theme.spacing.spacing16,
                    end = theme.spacing.spacing16,
                    top = theme.spacing.spacing16
                ),
            contentAlignment = Alignment.Center
        ) {
            when (type) {
                is Logo -> BrandLogo(type)
                is Greeting -> GreetingTopBar(type)
            }
            // Empty in release builds — the design has no header actions — so this measures 0x0
            // and the header is exactly the Figma block. Debug builds put the debug entry point
            // here, inside the 16dp margin and within the wordmark's own 49dp, costing no height.
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                TopBarActions(animateVisibility = false)
            }
        }
    }
}

/**
 * The stacked MINDERA / ALFIE wordmark, Figma `doc_branding` (160x49).
 *
 * Deliberately unsized: `brand_logo` carries that intrinsic size, and Material3 [Icon] only falls
 * back to its own 24dp default when the painter's intrinsic size is unspecified. Same treatment
 * as the startup screen.
 */
@Composable
private fun BrandLogo(type: Logo) {
    Icon(
        painter = painterResource(id = type.icon),
        contentDescription = type.contentDescription,
        tint = LocalTheme.current.color.content.contentPrimary,
        modifier = Modifier.testTag(HOME_TITLE_HEADER)
    )
}

@Composable
private fun GreetingTopBar(
    greetingType: Greeting
) {
    val theme = LocalTheme.current
    Column(
        modifier = Modifier.testTag(HOME_TITLE_HEADER),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.top_bar_greeting, greetingType.userName),
            style = theme.typography.heading.medium,
            color = theme.primitive.colors.neutrals900,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        greetingType.subtitle?.let {
            Text(
                text = it,
                style = theme.typography.body.small,
                color = theme.primitive.colors.neutrals500,
                textAlign = TextAlign.Center
            )
        }
    }
}

/** Idle Home header — the 145dp Figma block: 16 + 49 + 24 + 40 + 16. */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun LandingHeaderIdlePreview() {
    Theme {
        val searchState = rememberSearchState()
        TopBarScopeInstance(
            state = TopBarState(
                title = TopBarTitle.Custom(searchState) {},
                showNavigationIcon = false
            ),
            topBarColors = TopAppBarDefaults.topAppBarColors()
        ).LandingHeader(type = Logo())
    }
}

/** Search mode — branding collapsed, leaving the 48dp band with its back button and divider. */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun LandingHeaderSearchModePreview() {
    Theme {
        val searchState = rememberSearchState().apply { updateSearchState(true) }
        TopBarScopeInstance(
            state = TopBarState(
                title = TopBarTitle.Custom(searchState) {},
                showNavigationIcon = false
            ),
            topBarColors = TopAppBarDefaults.topAppBarColors()
        ).LandingHeader(type = Logo())
    }
}

/** The legacy greeting branch — no Figma counterpart, kept rendering for the debug catalog. */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun LandingHeaderGreetingPreview() {
    Theme {
        val searchState = rememberSearchState()
        TopBarScopeInstance(
            state = TopBarState(
                title = TopBarTitle.Custom(searchState) {},
                showNavigationIcon = false
            ),
            topBarColors = TopAppBarDefaults.topAppBarColors()
        ).LandingHeader(type = Greeting("User", "Member since: 1838"))
    }
}
