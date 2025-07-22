/*
 * Copyright 2025 Giste Trappiste
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.giste.profiles.ui

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class ProfilesDestinations {
    @Serializable
    internal data object ProfileManager : ProfilesDestinations()

    @Serializable
    internal data class ProfileDetail(
        val profileId: Long,
    ) : ProfilesDestinations()

    @Serializable
    internal data object NewProfile : ProfilesDestinations()
}

fun NavGraphBuilder.managerDestination(
    onNavigateToProfileDetails: (profileId: Long) -> Unit,
    onNavigateToNewProfileDialog: () -> Unit,
) {
    composable<ProfilesDestinations.ProfileManager> {
        val viewModel: ManagerViewModel = hiltViewModel()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
        ManagerScreen(
            state = uiState,
            onProfileAdd = { onNavigateToNewProfileDialog() },
            onProfileSelect = {},
            onProfileClick = { onNavigateToProfileDetails(it.id) },
            onProfileDelete = {
                viewModel.onUiAction(ManagerViewModel.UiAction.DeleteProfile(it))
            }
        )
    }
}

fun NavGraphBuilder.newProfileDestination(
    onDismiss: () -> Unit,
    onNewProfile: (profileId: Long) -> Unit,
) {
    dialog<ProfilesDestinations.NewProfile> {
        val viewModel: NewProfileViewModel = hiltViewModel()
        NewProfileDialog(
            uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
            onNameChange = { viewModel.onUiAction(NewProfileViewModel.UiAction.OnNameChange(it)) },
            onAccept = { viewModel.onUiAction(NewProfileViewModel.UiAction.OnAccept) },
            onDismiss = { onDismiss() },
            onNewProfile = { onNewProfile(it) },
        )
    }
}

fun NavGraphBuilder.profileDetailsDestination(
    onBackNavigation: () -> Unit, //TODO
) {
    composable<ProfilesDestinations.ProfileDetail> { navBackStackEntry ->
        Log.d("ProfileDetail", "NavStackEntry: $navBackStackEntry")
        val profileRoute: ProfilesDestinations.ProfileDetail = navBackStackEntry.toRoute()
        val viewModel: ProfileViewModel = hiltViewModel(
            creationCallback = { factory: ProfileViewModel.Factory ->
                factory.create(profileRoute.profileId)
            }
        )
        ProfileScreen(
            uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
            onValueChange = {
                viewModel.onUiAction(ProfileViewModel.UiAction.OnValueChange(it))
            }
        )
    }
}

fun NavController.navigateToProfileDetails(profileId: Long) {
    navigate(route = ProfilesDestinations.ProfileDetail(profileId = profileId))
}

fun NavController.navigateToNewProfileDialog() {
    navigate(route = ProfilesDestinations.NewProfile)
}
