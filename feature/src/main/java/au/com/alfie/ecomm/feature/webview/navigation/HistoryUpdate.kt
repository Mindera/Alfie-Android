package au.com.alfie.ecomm.feature.webview.navigation

data class HistoryUpdate(
    val to: String,
    val from: String,
    val cancelUpdate: () -> Unit
)
