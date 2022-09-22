package org.giste.profiles.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Insert
    suspend fun add(setting: SettingEntity): Long

    @Insert
    suspend fun add(settings: List<SettingEntity>)

    @Update
    suspend fun update(setting: SettingEntity)

    @Update
    suspend fun update(settings: List<SettingEntity>): Int

    @Query("SELECT * FROM settings WHERE profile_id = :profileId")
    fun findByProfileId(profileId: Long): Flow<List<SettingEntity>>
}