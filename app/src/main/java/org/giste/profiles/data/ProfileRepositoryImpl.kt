package org.giste.profiles.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileRepository

class ProfileRepositoryImpl(
    private val profileDao: ProfileDao,
    private val profileMapper: ProfileMapper,
    private val selectedProfileDao: SelectedProfileDao
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

    override fun findSelectedProfile(): Flow<Long> {
        return selectedProfileDao.findSelected()
            .map { it?.selectedId ?: 0L }
    }

    override suspend fun selectProfile(profile: Profile) {
        selectedProfileDao.selectProfile(SelectedProfileEntity(profile.id))
    }

    override suspend fun checkIfExists(name: String): Boolean {
        return profileDao.findByName(name) != null
    }

}