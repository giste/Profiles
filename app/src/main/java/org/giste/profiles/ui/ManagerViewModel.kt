package org.giste.profiles.ui

//import org.giste.profiles.domain.FindSelectedProfileUseCase
//import org.giste.profiles.domain.SelectProfileUseCase
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.giste.profiles.domain.DeleteProfileUseCase
import org.giste.profiles.domain.FindProfilesUseCase
import org.giste.profiles.domain.Profile
import javax.inject.Inject

@HiltViewModel
class ManagerViewModel @Inject constructor(
    findProfilesUseCase: FindProfilesUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase,
//    private val selectProfileUseCase: SelectProfileUseCase,
//    findSelectedProfileUseCase: FindSelectedProfileUseCase
) : ViewModel() {
    var profileList by mutableStateOf<List<Profile>>(listOf())
        private set
    var selectedProfileId by mutableStateOf(0L)

    init {
        findProfilesUseCase.invoke().onEach {
            Log.d("ManagerViewModel", "findProfilesUseCase: $it")
            profileList = it
        }.launchIn(viewModelScope)

//        findSelectedProfileUseCase.invoke().onEach {
//            Log.d("ManagerViewModel", "findSelectedProfileUseCase: $it")
//            selectedProfileId = it
//        }.launchIn(viewModelScope)
    }

    fun deleteProfile(profile: Profile) {
        viewModelScope.launch {
            deleteProfileUseCase.invoke(profile)
        }
    }

//    fun onProfileSelected(profile: Profile) {
//        selectProfile(profile)
//    }
//
//    private fun selectProfile(profile: Profile) {
//        viewModelScope.launch {
//            selectProfileUseCase.invoke(profile)
//        }
//    }
}