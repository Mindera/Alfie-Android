package au.com.alfie.ecomm.feature.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

@Composable
inline fun <reified T : ViewModel> NavController.sharedViewModel(): T {
    return this.currentBackStackEntry?.let { entry ->
        val navGraphRoute = entry.destination.parent?.route ?: return hiltViewModel()
        val parentEntry = remember(entry) {
            this.getBackStackEntry(navGraphRoute)
        }
        hiltViewModel(parentEntry)
    } ?: hiltViewModel()
}
