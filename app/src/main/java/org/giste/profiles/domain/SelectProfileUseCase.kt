package org.giste.profiles.domain

import javax.inject.Inject

class SelectProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(profile: Profile) =
        profileRepository.selectProfile(profile)
}