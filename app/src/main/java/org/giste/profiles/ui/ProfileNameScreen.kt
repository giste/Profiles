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
import org.giste.profiles.ui.components.TextDialog
import org.giste.profiles.ui.destinations.ProfileBodyDestination

@Preview
@Composable
fun ProfileNamePreview(){
    ProfileNameDialog(
        title = "New Profile",
        label = "Name",
        onAccept = {},
        onDismiss = { },
        getErrorForText = { "" }
    )
}

@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun ProfileNameBody(
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Long>
) {
    val profileNameViewModel: ProfileNameViewModel = hiltViewModel()

    LaunchedEffect("newProfile") {
        profileNameViewModel.newProfileIdFlow.onEach { newProfileId ->
            Log.d("ProfileManagerBody", "navigate(${ProfileBodyDestination.route}/$newProfileId)")
            resultNavigator.navigateBack(newProfileId)
        }.launchIn(this)
    }

    ProfileNameDialog(
        title = stringResource(id = R.string.profile_name_dialog_title),
        label = stringResource(id = R.string.profile_name_dialog_label),
        onAccept = { profileNameViewModel.onAccept(it) },
        onDismiss = { navigator.navigateUp() },
        getErrorForText = {
            if (it.isBlank()) {
                "Can't be empty"
            } else {
                ""
            }
        }
    )
}

@Composable
fun ProfileNameDialog(
    title: String,
    label: String,
    onAccept: (String) -> Unit,
    onDismiss: () -> Unit,
    getErrorForText: (String) -> String
) {
    TextDialog(
        title = title,
        onDismiss = onDismiss,
        onAccept = onAccept,
        label = label,
        getErrorForText = getErrorForText,
        maxLength = 20
    )
}