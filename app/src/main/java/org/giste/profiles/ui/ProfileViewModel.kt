package org.giste.profiles.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.giste.profiles.domain.*
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
        findProfileByIdUseCase.invoke(id).onEach {
            Log.d("ProfileViewModel", "Profile found: $it")
            profile = it
        }.launchIn(viewModelScope)
    }

    fun onOverrideChange(type: SettingType, override: Boolean) {
        Log.d("ProfileViewModel", "onOverrideChange($type, $override)")

        viewModelScope.launch {
            val newSettings = profile.settings.toMutableMap()
            with(profile.settings[type]) {
                newSettings[type] = when (type) {
                    SettingType.VOLUME_MEDIA,
                    SettingType.VOLUME_RING,
                    SettingType.VOLUME_NOTIFICATION,
                    SettingType.VOLUME_ALARM -> (this as IntSetting).copy(override = override)
                    SettingType.RING_MODE -> (this as RingModeSetting).copy(override = override)
                    SettingType.CONNECTION_WIFI,
                    SettingType.CONNECTION_DATA,
                    SettingType.CONNECTION_BLUETOOTH,
                    SettingType.CONNECTION_NFC,
                    SettingType.CONNECTION_AIRPLANE -> (this as BooleanSetting).copy(override = override)
                }
            }

            updateProfileUseCase.invoke(profile.copy(settings = newSettings.toMap()))
        }
    }

    fun onValueChange(type: SettingType, value: Any) {
        Log.d("ProfileViewModel", "onValueChange($type, $value)")

        viewModelScope.launch {
            val newSettings = profile.settings.toMutableMap()
            with(profile.settings[type]) {
                newSettings[type] = when (type) {
                    SettingType.VOLUME_MEDIA,
                    SettingType.VOLUME_RING,
                    SettingType.VOLUME_NOTIFICATION,
                    SettingType.VOLUME_ALARM -> (this as IntSetting).copy(value = value as Int)
                    SettingType.RING_MODE -> (this as RingModeSetting).copy(value = value as RingModeSetting.Companion.RingMode)
                    SettingType.CONNECTION_WIFI,
                    SettingType.CONNECTION_DATA,
                    SettingType.CONNECTION_BLUETOOTH,
                    SettingType.CONNECTION_NFC,
                    SettingType.CONNECTION_AIRPLANE -> (this as BooleanSetting).copy(value = value as Boolean)
                }
            }

            updateProfileUseCase.invoke(profile.copy(settings = newSettings.toMap()))
        }
    }
}