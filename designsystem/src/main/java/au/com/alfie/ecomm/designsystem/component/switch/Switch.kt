package au.com.alfie.ecomm.designsystem.component.switch

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.animation.animateAlignmentAsState
import au.com.alfie.ecomm.designsystem.theme.Theme

private const val DISABLED_ALPHA = .25f

@Composable
fun Switch(
    checked: Boolean,
    onCheckChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }

    val trackAnimatedColor = animateColorAsState(
        targetValue = if (checked) Theme.color.secondary.green050 else Theme.color.primary.mono200,
        label = "Track Color Animation"
    )

    val borderAnimatedColor = animateColorAsState(
        targetValue = if (checked) Theme.color.secondary.green600 else Theme.color.primary.mono050,
        label = "Border Color Animation"
    )

    val alignmentAnimated = animateAlignmentAsState(
        targetAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
    )

    val toggleableModifier = Modifier
        .toggleable(
            value = checked,
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            role = Role.Switch
        ) {
            onCheckChange(!checked)
        }

    val trackModifier = Modifier
        .alpha(if (enabled) 1f else DISABLED_ALPHA)
        .background(
            color = trackAnimatedColor.value,
            shape = RoundedCornerShape(percent = 100)
        )
        .border(
            width = 1.dp,
            color = borderAnimatedColor.value,
            shape = RoundedCornerShape(percent = 100)
        )

    Box(
        modifier = modifier
            .requiredSize(
                width = 56.dp,
                height = 32.dp
            )
            .then(toggleableModifier)
            .then(trackModifier),
        contentAlignment = alignmentAnimated.value
    ) {
        Box(
            modifier = Modifier
                .padding(Theme.spacing.spacing4)
                .size(24.dp)
                .background(
                    color = Theme.color.primary.mono700,
                    shape = CircleShape
                )
                .indication(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current
                )
        )
    }
}

@Preview
@Composable
private fun SwitchPreview() {
    Column {
        Switch(checked = true, onCheckChange = {})
        Switch(checked = true, onCheckChange = {}, enabled = false)
        Switch(checked = false, onCheckChange = {}, enabled = false)
    }
}
