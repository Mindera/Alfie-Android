package com.mindera.alfie.feature.scanner.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.scanner.R

/** Reticle transparency — the frame reads as a guide over the preview, not as chrome. */
private const val RETICLE_ALPHA = 0.6F

/** A wide, shallow window: retail barcodes are much wider than they are tall. */
private const val RETICLE_ASPECT_RATIO = 1.6F

/**
 * Chrome drawn over the camera preview: a framing reticle, a hint, and a close affordance.
 *
 * The close button and hint sit *inside* this overlay rather than in the app shell's top bar
 * because the scanner hides both bars — and the shell's nested `systemBarsPadding()` means the
 * preview is inset from the system bars anyway, so shell-level chrome would look orphaned.
 */
@Composable
internal fun ScannerOverlay(
    onCloseClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(theme.spacing.spacing32),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(RETICLE_ASPECT_RATIO)
                    .alpha(RETICLE_ALPHA)
                    .border(
                        width = theme.primitive.border.weightDefault,
                        color = theme.primitive.colors.neutrals0,
                        shape = theme.sizing.radius.soft
                    )
            )
            Text(
                text = stringResource(id = R.string.scanner_hint),
                style = theme.typography.body.medium,
                color = theme.primitive.colors.neutrals0,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = theme.spacing.spacing24)
            )
        }

        ScannerCloseButton(
            onClick = onCloseClick,
            modifier = Modifier.align(Alignment.TopStart),
            tint = theme.primitive.colors.neutrals0
        )
    }
}
