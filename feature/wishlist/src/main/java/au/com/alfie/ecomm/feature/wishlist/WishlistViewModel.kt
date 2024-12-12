package au.com.alfie.ecomm.feature.wishlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import au.com.alfie.ecomm.core.navigation.arguments.wishlist.WishlistNavArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val args: WishlistNavArgs = savedStateHandle.navArgs()
    val launchFromTop: Boolean = args.launchFromTop
}
