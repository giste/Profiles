package org.giste.profiles.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.giste.profiles.R
import org.giste.profiles.ui.components.TextDialogScreen
import org.giste.profiles.ui.destinations.ProfileScreenDestination

@Preview
@Composable
fun ProfileNamePreview() {
    ProfileNameContent(
        title = "New Profile",
        label = "Name",
        onAccept = {},
        onDismiss = { },
        error = "Error text",
        onValidate = {}
    )
}

@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun ProfileNameDialog(
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Long>
) {
    val profileNameViewModel: ProfileNameViewModel = hiltViewModel()

    LaunchedEffect("newProfile") {
        profileNameViewModel.newProfileIdFlow.onEach { newProfileId ->
            Log.d("ProfileManagerBody", "navigate(${ProfileScreenDestination.route}/$newProfileId)")
            resultNavigator.navigateBack(newProfileId)
        }.launchIn(this)
    }

    ProfileNameContent(
        title = stringResource(id = R.string.profile_name_dialog_title),
        label = stringResource(id = R.string.profile_name_dialog_label),
        onAccept = { profileNameViewModel.onAccept(it) },
        onDismiss = { navigator.navigateUp() },
        error = stringResource(id = profileNameViewModel.errorResource),
        onValidate = profileNameViewModel::onValidate
    )
}

@Composable
private fun ProfileNameContent(
    title: String,
    label: String,
    onAccept: (String) -> Unit,
    onDismiss: () -> Unit,
    error: String,
    onValidate: (String) -> Unit
) {
    TextDialogScreen(
        title = title,
        onDismiss = onDismiss,
        onAccept = onAccept,
        label = label,
        initialText = "",
        error = error,
        onValidate = onValidate,
        maxLength = 20
    )
}