package au.com.alfie.ecomm.designsystem.animation

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable

private const val EMPHASIZED = 500
private const val EMPHASIZED_DECELERATE = 400
private const val EMPHASIZED_ACCELERATE = 200
private const val STANDARD = 300
private const val STANDARD_DECELERATE = 250
private const val STANDARD_ACCELERATE = 200

@Stable
fun <T> emphasized() = tween<T>(
    durationMillis = EMPHASIZED,
    easing = LinearEasing
)

@Stable
fun <T> emphasizedDecelerate() = tween<T>(
    durationMillis = EMPHASIZED_DECELERATE,
    easing = EaseOut
)

@Stable
fun <T> emphasizedAccelerate() = tween<T>(
    durationMillis = EMPHASIZED_ACCELERATE,
    easing = EaseIn
)

@Stable
fun <T> standard() = tween<T>(
    durationMillis = STANDARD,
    easing = LinearEasing
)

@Stable
fun <T> standardDecelerate() = tween<T>(
    durationMillis = STANDARD_DECELERATE,
    easing = EaseOut
)

@Stable
fun <T> standardAccelerate() = tween<T>(
    durationMillis = STANDARD_ACCELERATE,
    easing = EaseIn
)
