package au.com.alfie.ecomm.designsystem.component.badge

import androidx.compose.runtime.Stable

@Stable
sealed interface BadgeType {

    data object Highlight : BadgeType

    data class Counter(val count: Int) : BadgeType

    data object None : BadgeType
}
