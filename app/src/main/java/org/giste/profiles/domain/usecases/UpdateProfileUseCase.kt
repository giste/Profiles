package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(profile: Profile) {
        val updatedProfile = if (profile.autoBrightness.apply && profile.autoBrightness.value) {
            profile.copy(
                brightness = profile.brightness.copy(apply = false)
            )
        } else {
            profile
        }
        profileRepository.update(updatedProfile)
    }
}