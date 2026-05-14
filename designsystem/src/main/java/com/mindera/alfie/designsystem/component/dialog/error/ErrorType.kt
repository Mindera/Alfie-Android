package com.mindera.alfie.designsystem.component.dialog.error

data class ErrorType(
    val message: String,
    val buttonLabel: String,
    var onButtonClick: (() -> Unit)? = null
)