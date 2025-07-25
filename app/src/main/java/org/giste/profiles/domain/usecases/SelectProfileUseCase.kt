package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.repositories.SelectedProfileRepository
import org.giste.profiles.domain.repositories.SystemRepository
import javax.inject.Inject

class SelectProfileUseCase @Inject constructor(
    private val selectedProfileRepository: SelectedProfileRepository,
    private val systemRepository: SystemRepository,
) {
    suspend operator fun invoke(profile: Profile) {
        selectedProfileRepository.selectProfile(profile)
        systemRepository.applySettings(profile)
    }
}