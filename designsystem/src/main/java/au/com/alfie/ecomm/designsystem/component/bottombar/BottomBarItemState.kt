package au.com.alfie.ecomm.designsystem.component.bottombar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import au.com.alfie.ecomm.designsystem.component.badge.BadgeType

@Composable
fun rememberBottomBarItemState(
    isSelected: Boolean = false,
    badge: BadgeType = BadgeType.None
) = remember {
    BottomBarItemState(
        isSelected = isSelected,
        badge = badge
    )
}

@Stable
class BottomBarItemState(
    isSelected: Boolean,
    badge: BadgeType
) {
    var isSelected by mutableStateOf(isSelected)
        private set

    var badge by mutableStateOf(badge)
        private set

    fun updateSelectedState(isSelected: Boolean) {
        this.isSelected = isSelected
    }

    fun updateBadge(badge: BadgeType) {
        this.badge = badge
    }
}
