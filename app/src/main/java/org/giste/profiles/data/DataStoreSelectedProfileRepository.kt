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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.giste.profiles.domain.SelectedProfileRepository
import javax.inject.Inject

private const val TAG = "DataStoreSelectedProfileRepository"

class DataStoreSelectedProfileRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SelectedProfileRepository {
    companion object {
        val SELECTED_PROFILE = longPreferencesKey("SELECTED_PROFILE")
    }

    override fun findSelectedProfile(): Flow<Long> {
        Log.d(TAG, "Reading selected profile")

        return dataStore.data.map {
            it[SELECTED_PROFILE] ?: 0
        }.distinctUntilChanged()
    }

    override suspend fun selectProfile(id: Long) {
        Log.d(TAG, "Saving selected profile with id = $id")

        dataStore.edit {
            it[SELECTED_PROFILE] = id
        }
    }
}