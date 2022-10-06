package org.giste.profiles.domain.usecases

import android.util.Log
import kotlinx.coroutines.flow.*
import org.giste.profiles.domain.*
import javax.inject.Inject

class FindProfileByIdUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke(id: Long) = profileRepository.findById(id).onEach { profile ->
        if (profile.settings.size != SettingType.values().size) {
            // Missing settings, add them
            SettingType.values().forEach {
                if (!profile.settings.containsKey(it)) {
                    Log.d("FindProfileByIdUseCase", "Adding setting: $it")
                    profileRepository.addSetting(
                        when (it) {
                            SettingType.VOLUME_MEDIA,
                            SettingType.VOLUME_RING,
                            SettingType.VOLUME_NOTIFICATION,
                            SettingType.VOLUME_ALARM -> IntSetting(
                                profileId = profile.id,
                                type = it
                            )
                            SettingType.RING_MODE -> RingModeSetting(
                                profileId = profile.id,
                                type = it
                            )
                        }
                    )
                }
            }
        }
    }
}