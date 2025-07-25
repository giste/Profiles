package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.repositories.ProfileRepository
import javax.inject.Inject

class DeleteProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(profile: Profile) = profileRepository.delete(profile)
}