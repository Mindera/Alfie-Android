package com.mindera.alfie.feature.scanner.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.scanner.R

/**
 * Chrome drawn over the camera preview — Figma "Scan barcode camera" (node `3353:35094`).
 *
 * The design dims the whole frame to 60% black and floats a blurred header over it. Values below
 * come from the node's own variables where one exists (`border/medium`, `content/content-inverted-
 * primary`, `spacing` steps, `screen-size/margin`) and from its measured geometry where it does not —
 * the reticle's 320x143 box and the 2dp stroke are drawn dimensions, not tokens.
 */
@Composable
internal fun ScannerOverlay(
    isTorchOn: Boolean,
    onBackClick: ClickEvent,
    onTorchClick: ClickEvent,
    onEnterManuallyClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current

    Box(
        modifier = modifier
            .fillMaxSize()
            // Figma paints the status bar, content and navigation bands all at rgba(0,0,0,0.6),
            // i.e. one scrim across the whole frame rather than a cut-out around the reticle.
            .background(SCRIM)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = theme.spacing.spacing16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Reticle()
            Column(
                modifier = Modifier
                    .padding(top = theme.spacing.spacing40)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing24)
            ) {
                Text(
                    text = stringResource(id = R.string.scanner_hint),
                    style = theme.typography.body.medium,
                    color = theme.color.content.contentInvertedPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                EnterManuallyButton(onClick = onEnterManuallyClick)
            }
        }

        ScannerHeader(
            isTorchOn = isTorchOn,
            onBackClick = onBackClick,
            onTorchClick = onTorchClick,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

/**
 * The framing window. Fixed 320x143 as drawn — it is a camera sighting box, so it keeps its
 * aspect rather than flexing with the screen; the 16dp screen margin still bounds it on narrow
 * devices. Radius reuses `spacing/spacing-sm` (12), exactly as the Figma node does.
 */
@Composable
private fun Reticle(modifier: Modifier = Modifier) {
    val theme = LocalTheme.current
    Box(
        modifier = modifier
            .widthIn(max = RETICLE_WIDTH)
            .fillMaxWidth()
            .height(RETICLE_HEIGHT)
            .border(
                width = RETICLE_STROKE,
                color = theme.color.border.medium,
                shape = RoundedCornerShape(theme.spacing.spacing12)
            )
    )
}

/**
 * Secondary button inverted for the camera: transparent fill with a white stroke and label.
 * The design-system [com.mindera.alfie.designsystem.component.button.Button] secondary variant
 * draws its stroke and label from `neutrals800`, which disappears against the preview, and it has
 * no inverted variant — so this stays local to the scanner rather than bending a shared component.
 */
@Composable
private fun EnterManuallyButton(
    onClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    Box(
        modifier = modifier
            .widthIn(min = MANUAL_BUTTON_MIN_WIDTH)
            .border(
                width = theme.primitive.border.weightDefault,
                color = theme.color.content.contentInvertedPrimary
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = theme.spacing.spacing16,
                vertical = theme.spacing.spacing8
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.scanner_enter_manually),
            style = theme.typography.body.medium,
            color = theme.color.content.contentInvertedPrimary,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Floating header — back, title, torch — matching the Figma "Header" node: 48dp tall, 4dp inset,
 * 8dp gaps, tertiary (transparent) buttons around 24dp glyphs.
 *
 * The design applies a background blur behind it. Compose has no equivalent that works over an
 * `AndroidView` camera surface, so the scrim underneath carries the contrast instead.
 */
@Composable
private fun ScannerHeader(
    isTorchOn: Boolean,
    onBackClick: ClickEvent,
    onTorchClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = HEADER_MIN_HEIGHT)
            .padding(theme.spacing.spacing4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing8)
    ) {
        HeaderIconButton(
            icon = AlfieIcons.ChevronLeft,
            contentDescription = stringResource(id = R.string.scanner_back_content_description),
            onClick = onBackClick
        )
        Text(
            text = stringResource(id = R.string.scanner_title),
            style = theme.typography.heading.xSmall,
            color = theme.color.content.contentPrimaryActive,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )
        HeaderIconButton(
            icon = AlfieIcons.Bolt,
            contentDescription = stringResource(
                id = if (isTorchOn) {
                    R.string.scanner_torch_off_content_description
                } else {
                    R.string.scanner_torch_on_content_description
                }
            ),
            // The glyph has no filled counterpart, so the on-state reads through colour: the
            // brand accent against the otherwise white header.
            tint = if (isTorchOn) {
                theme.primitive.colors.brandSelfridges500
            } else {
                theme.color.content.contentPrimaryActive
            },
            onClick = onTorchClick
        )
    }
}

@Composable
private fun HeaderIconButton(
    icon: Int,
    contentDescription: String,
    onClick: ClickEvent,
    modifier: Modifier = Modifier,
    tint: Color = LocalTheme.current.color.content.contentPrimaryActive
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(Theme.iconSize.large)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(Theme.iconSize.medium)
        )
    }
}

private val SCRIM = Color.Black.copy(alpha = 0.6F)

/** Drawn geometry from the Figma node — a sighting box, with no token behind it. */
private val RETICLE_WIDTH = 320.dp
private val RETICLE_HEIGHT = 143.dp
private val RETICLE_STROKE = 2.dp
private val MANUAL_BUTTON_MIN_WIDTH = 137.dp
private val HEADER_MIN_HEIGHT = 48.dp
