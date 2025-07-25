package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.repositories.SystemRepository
import javax.inject.Inject

class FindSystemPropertiesUseCase @Inject constructor(
    private val systemRepository: SystemRepository,
) {
    operator fun invoke() = systemRepository.systemProperties
}