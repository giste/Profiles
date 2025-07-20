package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.ProfileRepository
import javax.inject.Inject

class NameExistsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(name: String) = profileRepository.nameExists(name)
}