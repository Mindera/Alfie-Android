package au.com.alfie.ecomm.debug.operational.view.logcat

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import au.com.alfie.ecomm.debug.operational.R
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet

@Destination(style = DestinationStyleBottomSheet::class)
@Composable
internal fun LogcatBottomSheet() {
    val viewModel = hiltViewModel<LogcatViewModel>()
    val log by viewModel.log.collectAsStateWithLifecycle()

    Logcat(log = log)
}

@Composable
private fun Logcat(log: List<String>) {
    Surface(
        modifier = Modifier
            .nestedScroll(rememberNestedScrollInteropConnection())
            .padding(horizontal = Theme.spacing.spacing4)
    ) {
        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
                Text(
                    text = stringResource(R.string.log_bottom_sheet_label),
                    style = Theme.typography.smallBold
                )
                Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
            }
            items(items = log) {
                Text(
                    text = it,
                    style = Theme.typography.tiny
                )
            }
        }
    }
}
