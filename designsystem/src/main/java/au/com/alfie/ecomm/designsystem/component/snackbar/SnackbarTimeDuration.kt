package au.com.alfie.ecomm.designsystem.component.snackbar

enum class SnackbarTimeDuration(val milliseconds: Long) {
    LONG(milliseconds = 10000L),
    SHORT(milliseconds = 4000L),
    INDEFINITE(milliseconds = -1)
}
