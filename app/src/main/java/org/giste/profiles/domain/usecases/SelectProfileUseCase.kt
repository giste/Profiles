package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.SelectedProfileRepository
import javax.inject.Inject

class SelectProfileUseCase @Inject constructor(
    private val selectedProfileRepository: SelectedProfileRepository,
) {
    suspend operator fun invoke(id: Long) {
        selectedProfileRepository.selectProfile(id)
    }
}