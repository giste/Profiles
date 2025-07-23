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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.usecases.DeleteProfileUseCase
import org.giste.profiles.domain.usecases.FindAllProfilesUseCase
import org.giste.profiles.domain.usecases.FindSelectedProfileUseCase
import org.giste.profiles.domain.usecases.SelectProfileUseCase
import javax.inject.Inject

private const val TAG = "ManagerViewModel"

@HiltViewModel
class ManagerViewModel @Inject constructor(
    findAllProfilesUseCase: FindAllProfilesUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase,
    findSelectedProfileUseCase: FindSelectedProfileUseCase,
    private val selectProfileUseCase: SelectProfileUseCase,
) : ViewModel() {
    val uiState: StateFlow<UiState> = combine(
        findAllProfilesUseCase(),
        findSelectedProfileUseCase(),
    ) { profiles, selected ->
        val newUiState = UiState(profiles, selected)
        Log.d(TAG, "New state: $newUiState")
        return@combine newUiState
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState(),
    )

    data class UiState(
        val profiles: List<Profile> = emptyList(),
        val selectedProfile: Long = 0,
    )

    sealed class UiAction {
        data class SelectProfile(val profile: Profile): UiAction()
        data class DeleteProfile(val profile: Profile): UiAction()
    }

    fun onUiAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.SelectProfile -> selectProfile(uiAction.profile)
            is UiAction.DeleteProfile -> deleteProfile(uiAction.profile)
        }
    }

    private fun selectProfile(profile: Profile) = viewModelScope.launch {
        selectProfileUseCase(profile)
    }

    private fun deleteProfile(profile: Profile) = viewModelScope.launch {
        deleteProfileUseCase(profile)
    }
}