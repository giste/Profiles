package org.giste.profiles.domain

import javax.inject.Inject

class CheckIfProfileExistsUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(name: String) = profileRepository.checkIfExists(name)
}