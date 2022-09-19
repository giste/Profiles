package org.giste.profiles.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedProfileDao {
    @Query("SELECT * FROM SELECTED_PROFILE LIMIT 1")
    fun findSelected(): Flow<SelectedProfileEntity?>

    @Query("DELETE FROM SELECTED_PROFILE")
    suspend fun deleteSelected()

    @Insert
    suspend fun insert(selectedProfileDto: SelectedProfileEntity)

    @Transaction
    suspend fun selectProfile(selectedProfileDto: SelectedProfileEntity) {
        deleteSelected()
        insert(selectedProfileDto)
    }
}