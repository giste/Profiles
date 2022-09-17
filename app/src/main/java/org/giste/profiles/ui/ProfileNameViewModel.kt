package org.giste.profiles.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.giste.profiles.domain.AddProfileUseCase
import org.giste.profiles.domain.Profile
import javax.inject.Inject

@HiltViewModel
class ProfileNameViewModel @Inject constructor(
    private val addProfileUseCase: AddProfileUseCase
) : ViewModel() {
    private val _newProfileIdFlow = MutableSharedFlow<Long>(extraBufferCapacity = 1)
    val newProfileIdFlow = _newProfileIdFlow.asSharedFlow()

    fun onAccept(name: String) {
        viewModelScope.launch {
            val newProfileId = addProfileUseCase.invoke(Profile(name = name))
            _newProfileIdFlow.tryEmit(newProfileId)
        }
    }
}