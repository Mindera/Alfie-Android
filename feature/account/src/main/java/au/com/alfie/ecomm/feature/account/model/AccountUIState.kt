package au.com.alfie.ecomm.feature.account.model

internal sealed interface AccountUIState {

    data object Loading : AccountUIState

    data object Error : AccountUIState

    data class Loaded(val accountUI: AccountUI) : AccountUIState
}
