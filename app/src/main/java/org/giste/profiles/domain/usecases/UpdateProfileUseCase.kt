package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(profile: Profile) = profileRepository.update(
        profile = if (profile.autoBrightness.apply && profile.autoBrightness.value) {
            // Do not apply brightness level settings if auto-brightness is on
            profile.copy(brightness = profile.brightness.copy(apply = false))
        } else {
            profile
        }
    )
}