package org.giste.profiles.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.giste.profiles.domain.Profile

interface ProfileRepository {
    fun findAll(): Flow<List<Profile>>
    fun findById(id: Long): Flow<Profile>
    suspend fun add(profile: Profile): Long
    suspend fun update(profile: Profile)
    suspend fun delete(profile: Profile)
    suspend fun nameExists(name: String): Boolean
}