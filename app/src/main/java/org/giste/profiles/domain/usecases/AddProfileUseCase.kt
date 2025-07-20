package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileRepository
import javax.inject.Inject

class AddProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(profile: Profile) = profileRepository.add(profile)
}