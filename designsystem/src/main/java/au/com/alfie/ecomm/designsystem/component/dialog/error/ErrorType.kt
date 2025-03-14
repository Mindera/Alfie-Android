package au.com.alfie.ecomm.designsystem.component.dialog.error

data class ErrorType(
    val message: String,
    val buttonLabel: String,
    var onButtonClick: (() -> Unit)? = null
)