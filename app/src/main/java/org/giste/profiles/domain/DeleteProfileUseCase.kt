package org.giste.profiles.domain

import javax.inject.Inject

class DeleteProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(profile: Profile) = profileRepository.delete(profile)
}