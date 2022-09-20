package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.ProfileRepository
import javax.inject.Inject

class FindProfilesUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke() = profileRepository.findAll()
}