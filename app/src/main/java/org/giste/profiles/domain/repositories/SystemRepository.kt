package org.giste.profiles.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.SystemProperties

interface SystemRepository {
    val systemProperties: Flow<SystemProperties>
    suspend fun applySettings(profile: Profile)
}