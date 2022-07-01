package org.giste.profiles.domain

import javax.inject.Inject

class FindProfilesUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke() = profileRepository.findAll()
}