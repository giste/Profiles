package org.giste.profiles.domain

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun findAll(): Flow<List<Profile>>

    fun findById(id: Long): Flow<ProfileDetail>

    suspend fun add(profileDetail: ProfileDetail): Long

    suspend fun update(profileDetail: ProfileDetail): Int

    suspend fun delete(profile: Profile)

    fun findSelectedProfile(): Flow<Long>

    suspend fun selectProfile(profile: Profile)

    suspend fun checkIfExists(name: String): Boolean

    suspend fun addSetting(setting: Setting)
}