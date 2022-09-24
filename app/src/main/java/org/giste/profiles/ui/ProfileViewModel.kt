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
import org.giste.profiles.domain.ProfileDetail
import org.giste.profiles.domain.SettingType
import org.giste.profiles.domain.SystemProperties
import org.giste.profiles.domain.usecases.FindProfileByIdUseCase
import org.giste.profiles.domain.usecases.UpdateProfileUseCase
import org.giste.profiles.ui.destinations.ProfileScreenDestination
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    findProfileByIdUseCase: FindProfileByIdUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    val systemProperties: SystemProperties,
    state: SavedStateHandle
) : ViewModel() {
    var profile by mutableStateOf(ProfileDetail())
        private set
    private var id: Long = ProfileScreenDestination.argsFrom(state).id

    init {
        viewModelScope.launch {
            findProfileByIdUseCase.invoke(id).onEach { profile = it }.collect()
        }
    }

    fun onOverrideChange(type: SettingType, value: Boolean) {
        viewModelScope.launch {
            val updatedProfile = when (type) {
                SettingType.VOLUME_MEDIA ->
                    profile.copy(
                        mediaVolume = profile.mediaVolume.copy(override = value)
                    )
                SettingType.VOLUME_RING ->
                    profile.copy(
                        ringVolume = profile.ringVolume.copy(override = value)
                    )
                SettingType.VOLUME_NOTIFICATION ->
                    profile.copy(
                        notificationVolume = profile.notificationVolume.copy(override = value)
                    )
                SettingType.VOLUME_ALARM ->
                    profile.copy(
                        alarmVolume = profile.alarmVolume.copy(override = value)
                    )
            }

            updateProfileUseCase.invoke(updatedProfile)
        }
    }

    fun onValueChange(type: SettingType, value: Any) {
        viewModelScope.launch {
            val updatedProfile = when (type) {
                SettingType.VOLUME_MEDIA ->
                    profile.copy(
                        mediaVolume = profile.mediaVolume.copy(value = value as Int)
                    )
                SettingType.VOLUME_RING ->
                    profile.copy(
                        ringVolume = profile.ringVolume.copy(value = value as Int)
                    )
                SettingType.VOLUME_NOTIFICATION ->
                    profile.copy(
                        notificationVolume = profile.notificationVolume.copy(value = value as Int)
                    )
                SettingType.VOLUME_ALARM ->
                    profile.copy(
                        alarmVolume = profile.alarmVolume.copy(value = value as Int)
                    )
            }

            Log.d("onValueChange", "updating: $updatedProfile")
            updateProfileUseCase.invoke(updatedProfile)
        }
    }
}