package org.giste.profiles.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.giste.profiles.domain.*

class ProfileRepositoryImpl(
    private val profileDao: ProfileDao,
    private val profileMapper: ProfileMapper,
    private val selectedProfileDao: SelectedProfileDao,
    private val profileDetailDao: ProfileDetailDao,
    private val profileDetailMapper: ProfileDetailMapper,
    private val settingMapper: SettingMapper
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
        return profileDetailDao.add(
            profileMapper.toEntity(profileDetail),
            settingMapper.toEntity(getSettingMap(profileDetail))
        )
    }

    override suspend fun update(profileDetail: ProfileDetail): Int {
        return profileDetailDao.update(
            profileMapper.toEntity(profileDetail),
            settingMapper.toEntity(getSettingMap(profileDetail))
        )
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

    private fun getSettingMap(profileDetail: ProfileDetail): Map<SettingType, Setting<Any>> {
        with(profileDetail) {
            return mapOf(
                SettingType.VOLUME_MEDIA to mediaVolume,
                SettingType.VOLUME_RING to ringVolume,
                SettingType.VOLUME_NOTIFICATION to notificationVolume,
                SettingType.VOLUME_ALARM to alarmVolume
            )
        }
    }
}