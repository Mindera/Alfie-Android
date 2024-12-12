package au.com.alfie.ecomm.designsystem.animation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment

@Composable
fun animateAlignmentAsState(targetAlignment: Alignment): State<Alignment> = with(targetAlignment as BiasAlignment) {
    val horizontal by animateFloatAsState(horizontalBias, label = "Horizontal Bias Animation")
    val vertical by animateFloatAsState(verticalBias, label = "Vertical Bias Animation")

    return remember { derivedStateOf { BiasAlignment(horizontal, vertical) } }
}
