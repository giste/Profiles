package org.giste.profiles.domain

import javax.inject.Inject

class FindSelectedProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke() = profileRepository.findSelectedProfile()
}