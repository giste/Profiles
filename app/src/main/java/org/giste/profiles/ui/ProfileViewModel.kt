package org.giste.profiles.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.giste.profiles.domain.AddProfileUseCase
import org.giste.profiles.domain.FindProfileByIdUseCase
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.UpdateProfileUseCase
import org.giste.profiles.ui.destinations.ProfileBodyDestination
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    findProfileByIdUseCase: FindProfileByIdUseCase,
    private val addProfileUseCase: AddProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
//    private val deleteProfileUseCase: DeleteProfileUseCase,
    state: SavedStateHandle
) : ViewModel() {
    var profile by mutableStateOf(Profile())
        private set
    private var id: Long = ProfileBodyDestination.argsFrom(state).id

    init {
        viewModelScope.launch {
            if (id == 0L) {
                id = addProfileUseCase.invoke(Profile(name = "<New Profile>"))
            }

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

//    fun deleteProfile() {
//        if (profile.id != 0L) {
//            viewModelScope.launch {
//                deleteProfileUseCase.invoke(profile)
//            }
//        }
//    }

}