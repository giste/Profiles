package org.giste.profiles.domain

class FindProfilesUseCase(private val profileRepository: ProfileRepository) {
    operator fun invoke() = profileRepository.findAll()
}