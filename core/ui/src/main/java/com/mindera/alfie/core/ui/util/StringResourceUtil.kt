package com.mindera.alfie.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.commons.string.toString

@Composable
fun stringResource(resource: StringResource): String {
    val context = LocalContext.current
    return resource.toString(context)
}
