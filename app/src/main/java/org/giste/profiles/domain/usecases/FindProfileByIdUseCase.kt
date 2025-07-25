package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.repositories.ProfileRepository
import javax.inject.Inject

class FindProfileByIdUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(id: Long) = profileRepository.findById(id)
}