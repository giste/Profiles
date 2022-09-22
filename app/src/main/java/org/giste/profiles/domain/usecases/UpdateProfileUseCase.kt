package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.ProfileDetail
import org.giste.profiles.domain.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(profile: ProfileDetail) = profileRepository.update(profile)
}