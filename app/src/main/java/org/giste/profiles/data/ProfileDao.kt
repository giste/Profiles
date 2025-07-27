/*
 * Copyright 2025 Giste Trappiste
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.giste.profiles.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles ORDER BY profiles.name")
    fun findAll(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE profile_id = :id")
    fun findById(id: Long): Flow<ProfileEntity?>

    @Insert
    suspend fun add(profile: ProfileEntity): Long

    @Update
    suspend fun update(profile: ProfileEntity): Int

    @Delete
    suspend fun delete(profile: ProfileEntity): Int

    @Query("SELECT * FROM profiles WHERE name = :name")
    suspend fun findByName(name: String): ProfileEntity?
}