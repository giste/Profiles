package org.giste.profiles.domain

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun findAll(): Flow<List<Profile>>

    fun findById(id: Long): Flow<Profile>

    suspend fun add(profile: Profile): Long

    suspend fun update(profile: Profile): Int

    suspend fun delete(profile: Profile)

    fun findSelectedProfile(): Flow<Long>

    suspend fun selectProfile(profile: Profile)
}