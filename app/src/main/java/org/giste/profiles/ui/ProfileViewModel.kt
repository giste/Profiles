package org.giste.profiles.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.giste.profiles.domain.FindProfileByIdUseCase
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.UpdateProfileUseCase
import org.giste.profiles.ui.destinations.ProfileScreenDestination
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    findProfileByIdUseCase: FindProfileByIdUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    state: SavedStateHandle
) : ViewModel() {
    var profile by mutableStateOf(Profile())
        private set
    private var id: Long = ProfileScreenDestination.argsFrom(state).id

    init {
        viewModelScope.launch {
            findProfileByIdUseCase.invoke(id).onEach { profile = it }.collect()
        }
    }

    fun onNameChange(name: String) {
        val updatedProfile = profile.copy(name = name)
        viewModelScope.launch {
            updateProfileUseCase.invoke(updatedProfile)
            Log.d("ProfileViewModel", "Updated profile: $updatedProfile")
        }
    }
}