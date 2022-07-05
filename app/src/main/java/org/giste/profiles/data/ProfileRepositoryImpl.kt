package org.giste.profiles.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileRepository

class ProfileRepositoryImpl(
    private val profileDao: ProfileDao,
    private val profileMapper: ProfileMapper
) : ProfileRepository {
    override fun findAll(): Flow<List<Profile>> {
        return profileDao.findAll().map { list -> list.map { profileMapper.toModel(it) } }
    }

    override fun findById(id: Long): Flow<Profile> {
        return profileDao.findById(id).map {
            it?.let { profileMapper.toModel(it) }
                ?: throw IllegalArgumentException("No profile for id = $id")
        }
    }

    override suspend fun add(profile: Profile): Long {
        return profileDao.add(profileMapper.toEntity(profile))
    }

    override suspend fun update(profile: Profile): Int {
        return profileDao.update(profileMapper.toEntity(profile))
    }

    override suspend fun delete(profile: Profile) {
        return profileDao.delete(profileMapper.toEntity(profile))
    }

}