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

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileNotFoundException
import org.giste.profiles.domain.ProfileRepository
import javax.inject.Inject

private const val TAG = "DaoProfileRepository"

class DaoProfileRepository @Inject constructor(
    private val profileDao: ProfileDao,
) : ProfileRepository {
    override fun findAll(): Flow<List<Profile>> = profileDao
        .findAll()
        .map {
            Log.d(TAG, "Profiles found: $it")
            it.map { entity -> entity.toModel() }
        }

    override fun findById(id: Long): Flow<Profile> = profileDao
        .findById(id)
        .map {
            it?.let { entity ->
                Log.i(TAG, "Found ${it.name} profile for id = $id")
                Log.d(TAG, "Found profile: $it")

                entity.toModel()
            } ?: throw ProfileNotFoundException(id)
        }

    override suspend fun add(profile: Profile): Long {
        val id = profileDao.add(profile.toEntity())

        Log.i(TAG, "Profile '${profile.name}' added with id = $id")
        Log.d(TAG, "Profile added: $profile")

        return id
    }

    override suspend fun update(profile: Profile) {
        val updated = profileDao.update(profile.toEntity())
        if (updated == 0) throw ProfileNotFoundException(profile.id)

        Log.i(TAG, "Profile '${profile.name}' updated")
        Log.d(TAG, "Profile updated: $profile")
    }

    override suspend fun delete(profile: Profile) {
        val deleted = profileDao.delete(profile.toEntity())
        if (deleted == 0) throw ProfileNotFoundException(profile.id)

        Log.i(TAG, "Profile '${profile.name}' deleted")
    }

    override suspend fun nameExists(name: String): Boolean {
        val profileFound = profileDao.findByName(name)
        Log.d(TAG, "Profile for name = '$name': $profileFound")

        return profileFound != null
    }
}