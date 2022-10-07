package org.giste.profiles.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileDetail
import org.giste.profiles.domain.ProfileRepository
import org.giste.profiles.domain.Setting

class ProfileRepositoryImpl(
    private val profileDao: ProfileDao,
    private val profileMapper: ProfileMapper,
    private val selectedProfileDao: SelectedProfileDao,
    private val profileDetailDao: ProfileDetailDao,
    private val profileDetailMapper: ProfileDetailMapper,
    private val settingDao: SettingDao,
    private val settingMapper: SettingMapper,
    private val systemSettingsDataSource: SystemSettingsDataSource
) : ProfileRepository {
    override fun findAll(): Flow<List<Profile>> {
        return profileDao.findAll().map { list -> list.map { profileMapper.toModel(it) } }
    }

    override fun findById(id: Long): Flow<ProfileDetail> {
        return profileDetailDao.findById(id).map { map ->
            profileDetailMapper.toModel(
                profileEntity = map.keys.first(),
                settingList = map.values.first()
            )
        }
    }

    override suspend fun add(profileDetail: ProfileDetail): Long {
        val entity = profileDetailMapper.toEntity(profileDetail)
        return profileDetailDao.add(entity.first, entity.second)
    }

    override suspend fun update(profileDetail: ProfileDetail): Int {
        val entity = profileDetailMapper.toEntity(profileDetail)
        return profileDetailDao.update(entity.first, entity.second)
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
        systemSettingsDataSource.applySettings(
            settingDao.findByProfileId(profile.id).first()
                .map(settingMapper::toModel)
        )
    }

    override suspend fun checkIfExists(name: String): Boolean {
        return profileDao.findByName(name) != null
    }

    override suspend fun addSettings(settings: List<Setting>) {
        settingDao.add(settings.map(settingMapper::toEntity))
    }

}