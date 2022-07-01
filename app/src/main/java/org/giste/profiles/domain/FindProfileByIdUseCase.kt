package org.giste.profiles.domain

import javax.inject.Inject

class FindProfileByIdUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke(id: Long) = profileRepository.findById(id)
}