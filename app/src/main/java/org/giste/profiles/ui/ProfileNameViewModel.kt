package org.giste.profiles.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.giste.profiles.R
import org.giste.profiles.domain.AddProfileUseCase
import org.giste.profiles.domain.CheckIfProfileExistsUseCase
import org.giste.profiles.domain.Profile
import javax.inject.Inject

@HiltViewModel
class ProfileNameViewModel @Inject constructor(
    private val addProfileUseCase: AddProfileUseCase,
    private val checkIfProfileExistsUseCase: CheckIfProfileExistsUseCase,
) : ViewModel() {
    private val _newProfileIdFlow = MutableSharedFlow<Long>(extraBufferCapacity = 1)
    val newProfileIdFlow = _newProfileIdFlow.asSharedFlow()

    var name by mutableStateOf("")
        private set
    var errorResource by mutableStateOf(R.string.validation_no_error)
        private set

    companion object {
        const val maxLength = 20
    }

    fun onAccept(name: String) {
        viewModelScope.launch {
            try {
                val newProfileId = addProfileUseCase.invoke(Profile(name = name))
                _newProfileIdFlow.tryEmit(newProfileId)
            } catch (e: Throwable) {
                Log.d("onAccept", "e: $e")

                if (e.message?.contains("profiles.name", true) == true) {
                    errorResource = R.string.profile_name_dialog_profile_exists
                }
            }
        }
    }

    fun onNameChange(name: String) {
        if (name.length <= maxLength) {
            this.name = name
        }

        validateName(name)
    }

    private fun validateName(name: String) {
        viewModelScope.launch {
            errorResource = if (name.isBlank()) {
                R.string.validation_string_not_blank
            } else {
                if (checkIfProfileExistsUseCase.invoke(name)) {
                    R.string.profile_name_dialog_profile_exists
                } else {
                    R.string.validation_no_error
                }
            }
        }
    }
}