package org.giste.profiles.domain.usecases

import org.giste.profiles.domain.SystemRepository
import javax.inject.Inject

class FindSystemPropertiesUseCase @Inject constructor(
    private val systemRepository: SystemRepository,
) {
    operator fun invoke() = systemRepository.systemProperties
}