package au.com.alfie.ecomm.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.commons.string.toString

@Composable
fun stringResource(resource: StringResource): String {
    val context = LocalContext.current
    return resource.toString(context)
}
