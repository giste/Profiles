package org.giste.profiles.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM PROFILES")
    fun findAll(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM PROFILES WHERE ID = :id")
    fun findById(id: Long): Flow<ProfileEntity?>

    @Insert
    suspend fun add(profile: ProfileEntity): Long

    @Update
    suspend fun update(profile: ProfileEntity): Int

    @Delete
    suspend fun delete(profile: ProfileEntity)

    @Query("SELECT * FROM profiles WHERE name = :name")
    suspend fun findByName(name: String): ProfileEntity?
}