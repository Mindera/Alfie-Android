package com.mindera.alfie.feature.debug

import androidx.lifecycle.ViewModel
import com.mindera.alfie.debug.view.DebugViewContent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class DebugViewModel @Inject constructor(
    val debugViewContent: DebugViewContent
) : ViewModel()
