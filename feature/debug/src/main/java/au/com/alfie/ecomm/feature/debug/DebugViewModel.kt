package au.com.alfie.ecomm.feature.debug

import androidx.lifecycle.ViewModel
import au.com.alfie.ecomm.debug.view.DebugViewContent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class DebugViewModel @Inject constructor(
    val debugViewContent: DebugViewContent
) : ViewModel()
