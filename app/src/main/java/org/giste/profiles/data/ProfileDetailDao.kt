package org.giste.profiles.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

@Dao
abstract class ProfileDetailDao(db: ProfilesDb) {
    private val profileDao = db.profileDao()
    private val settingDao = db.settingDao()

    @Transaction
    open suspend fun add(profile: ProfileEntity, settings: List<SettingEntity>): Long {
        val id = profileDao.add(profile)
        val updatedSettings = settings.asFlow()
            .map { it.copy(profileId = id) }
            .toList()

        settingDao.add(updatedSettings)

        return id
    }

    @Transaction
    open suspend fun update(profile: ProfileEntity, settings: List<SettingEntity>): Int {
        return settingDao.update(settings)
    }

    @Query(
        "SELECT * FROM profiles " +
                "INNER JOIN settings ON profiles.profile_id = settings.profile_id " +
                "WHERE profiles.profile_id = :id"
    )
    abstract fun findById(id: Long): Flow<Map<ProfileEntity, List<SettingEntity>>>

}