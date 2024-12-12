package au.com.alfie.ecomm.designsystem.component.chip

data class ChipProperties(
    val label: String,
    val chipType: ChipType = ChipType.REGULAR,
    val counter: Int? = null,
    val isSelected: Boolean,
    val isEnabled: Boolean,
    val isDismissible: Boolean = false
)
