package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.animation.emphasized
import au.com.alfie.ecomm.designsystem.animation.emphasizedAccelerate
import au.com.alfie.ecomm.designsystem.animation.emphasizedDecelerate
import au.com.alfie.ecomm.designsystem.animation.standard
import au.com.alfie.ecomm.designsystem.animation.standardAccelerate
import au.com.alfie.ecomm.designsystem.animation.standardDecelerate
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonSize
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MotionScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    var transitionToggle by remember { mutableStateOf(false) }

    var dropdownExpanded by remember { mutableStateOf(false) }
    var dropdownValue by remember { mutableStateOf("Emphasized") }

    var offsetCircle1 by remember { mutableStateOf(IntOffset.Zero) }
    var offsetCircle2 by remember { mutableStateOf(IntOffset.Zero) }
    var offsetCircle3 by remember { mutableStateOf(IntOffset.Zero) }

    var selectedAnimationSpecOffset: AnimationSpec<IntOffset> by remember { mutableStateOf(emphasized()) }
    var selectedAnimationSpecFloat: AnimationSpec<Float> by remember { mutableStateOf(emphasized()) }

    var calcOffsetCircle1 = remember { IntOffset.Zero }
    var calcOffsetCircle2 = remember { IntOffset.Zero }
    var calcOffsetCircle3 = remember { IntOffset.Zero }

    val scaleState by animateFloatAsState(
        targetValue = if (transitionToggle) 6f else 1f,
        animationSpec = selectedAnimationSpecFloat,
        label = "scale"
    )
    val offsetCircle1State by animateIntOffsetAsState(
        targetValue = offsetCircle1,
        animationSpec = selectedAnimationSpecOffset,
        label = "offset1"
    )
    val offsetCircle2State by animateIntOffsetAsState(
        targetValue = offsetCircle2,
        animationSpec = selectedAnimationSpecOffset,
        label = "offset2"
    )
    val offsetCircle3State by animateIntOffsetAsState(
        targetValue = offsetCircle3,
        animationSpec = selectedAnimationSpecOffset,
        label = "offset3"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.spacing8)
    ) {
        Column {
            Text(
                modifier = Modifier.padding(Theme.spacing.spacing12),
                text = "Options",
                style = Theme.typography.heading3
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(Theme.spacing.spacing24))
        }

        Box(contentAlignment = Alignment.TopCenter) {
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = dropdownValue,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Theme.color.primary.mono050,
                        focusedContainerColor = Theme.color.primary.mono050
                    ),
                    label = {
                        Text(
                            text = "Motion:",
                            style = Theme.typography.paragraph
                        )
                    }
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    MotionType.entries.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it.title,
                                    style = Theme.typography.small
                                )
                            },
                            onClick = {
                                dropdownValue = it.title
                                selectedAnimationSpecOffset = it.getAnimationSpec()
                                selectedAnimationSpecFloat = it.getAnimationSpec()
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    type = ButtonType.Primary,
                    text = "Preview Motion",
                    buttonSize = ButtonSize.Large,
                    onClick = {
                        transitionToggle = !transitionToggle
                        offsetCircle1 = if (transitionToggle) calcOffsetCircle1 else IntOffset.Zero
                        offsetCircle2 = if (transitionToggle) calcOffsetCircle2 else IntOffset.Zero
                        offsetCircle3 = if (transitionToggle) calcOffsetCircle3 else IntOffset.Zero
                    }
                )

                Spacer(modifier = Modifier.height(Theme.spacing.spacing24))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Circle(
                        color = Theme.color.secondary.blue300,
                        modifier = Modifier
                            .scale(scale = scaleState)
                            .offset { offsetCircle1State },
                        onGlobalPosition = { position, _, size ->
                            calcOffsetCircle1 = IntOffset(-(position.x.roundToInt() + size.width), 0)
                        }
                    )
                    Circle(
                        color = Theme.color.secondary.blue500,
                        modifier = Modifier
                            .scale(scale = scaleState)
                            .offset { offsetCircle2State },
                        onGlobalPosition = { position, bounds, _ ->
                            calcOffsetCircle2 = IntOffset(0, (bounds.y - position.y).roundToInt())
                        }
                    )
                    Circle(
                        color = Theme.color.secondary.blue700,
                        modifier = Modifier
                            .scale(scale = scaleState)
                            .offset { offsetCircle3State },
                        onGlobalPosition = { position, bounds, _ ->
                            calcOffsetCircle3 = IntOffset((bounds.x - position.x).roundToInt(), 0)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Circle(
    color: Color,
    onGlobalPosition: (position: Offset, bounds: Offset, size: IntSize) -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = Modifier
            .size(120.dp)
            .then(modifier)
            .onGloballyPositioned {
                onGlobalPosition(
                    it.positionInParent(),
                    it.boundsInWindow().bottomRight,
                    it.size
                )
            }
    ) {
        drawCircle(
            color = color,
            radius = 150f
        )
    }
}

private enum class MotionType(val title: String) {
    Emphasized("Emphasized"),
    EmphasizedDecelerate("Emphasized Decelerate"),
    EmphasizedAccelerate("Emphasized Accelerate"),
    Standard("Standard"),
    StandardDecelerate("Standard Decelerate"),
    StandardAccelerate("Standard Accelerate");

    fun <T> getAnimationSpec() = when (this) {
        Emphasized -> emphasized<T>()
        EmphasizedDecelerate -> emphasizedDecelerate()
        EmphasizedAccelerate -> emphasizedAccelerate()
        Standard -> standard()
        StandardDecelerate -> standardDecelerate()
        StandardAccelerate -> standardAccelerate()
    }
}

@Preview(showBackground = true)
@Composable
private fun MotionScreenPreview() {
    val topBarState = TopBarState(TopBarTitle.Text(""), false)
    MotionScreen(topBarState = topBarState)
}
