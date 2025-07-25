package org.giste.profiles.domain

import kotlinx.coroutines.flow.Flow

interface SystemRepository {
    val systemProperties: Flow<SystemProperties>
    suspend fun applySettings(profile: Profile)
}