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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.usecases.AddProfileUseCase
import org.giste.profiles.domain.usecases.NameExistsUseCase
import javax.inject.Inject

@HiltViewModel
class NewProfileViewModel @Inject constructor(
    private val addProfileUseCase: AddProfileUseCase,
    private val nameExistsUseCase: NameExistsUseCase,
) : ViewModel() {
    private val state: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = state.asStateFlow()

    data class UiState(
        val name: String = "",
        val error: NameError = NameError.BlankNameError,
        val id: Long = 0,
    )

    fun onUiAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.OnNameChange -> onNameChange(uiAction.name)
            is UiAction.OnAccept -> { onAccept() }
        }
    }

    private fun onNameChange(name: String) {
        if(name.isBlank()) {
            state.update {
                UiState(name, NameError.BlankNameError)
            }
        } else {
            viewModelScope.launch {
                if (nameExistsUseCase(name)) {
                    state.update { UiState(name, NameError.NameExistsError) }
                } else {
                    state.update { UiState(name, NameError.NoError) }
                }
            }
        }
    }

    private fun onAccept() {
        viewModelScope.launch {
            val id = addProfileUseCase(Profile(name = uiState.value.name))
            state.update { it.copy(id = id) }
        }
    }

    sealed class UiAction {
        data class OnNameChange(val name: String) : UiAction()
        data object OnAccept : UiAction()
    }

    sealed class NameError {
        data object NoError : NameError()
        data object BlankNameError : NameError()
        data object NameExistsError : NameError()
    }
}