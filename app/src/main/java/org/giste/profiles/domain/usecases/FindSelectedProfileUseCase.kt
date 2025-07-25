package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.repositories.SelectedProfileRepository
import javax.inject.Inject

class FindSelectedProfileUseCase @Inject constructor(
    private val selectedProfileRepository: SelectedProfileRepository,
) {
    operator fun invoke() = selectedProfileRepository.findSelectedProfile()
}