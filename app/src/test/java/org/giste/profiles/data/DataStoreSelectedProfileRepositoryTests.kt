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

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.giste.profiles.data.DataStoreSelectedProfileRepository.Companion.SELECTED_PROFILE
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.repositories.SelectedProfileRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.io.File

private const val TEST_DATASTORE: String = "test.preferences_pb"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataStoreSelectedProfileRepositoryTests {
    @TempDir
    private lateinit var temporaryFolder: File
    private val testDataStore = PreferenceDataStoreFactory.create(
        produceFile = { File(temporaryFolder, TEST_DATASTORE) },
    )
    private val selectedProfileRepository: SelectedProfileRepository =
        DataStoreSelectedProfileRepository(testDataStore)

    @AfterEach
    fun afterEach() = runTest {
        // Clear datastore
        testDataStore.edit { it.clear() }
    }

    @Test
    fun store_selected_profile_when_saved() = runTest {
        selectedProfileRepository.selectProfile(Profile(id = 1L))

        val actual = testDataStore.data.map {
            it[SELECTED_PROFILE] ?: 0L
        }.first()

        assertEquals(1L, actual)
    }

    @Test
    fun return_new_selected_profile_when_changed() = runTest {
        val selectedProfiles = mutableListOf<Long>()

        backgroundScope.launch {
            selectedProfileRepository.findSelectedProfile().toList(selectedProfiles)
        }

        selectedProfileRepository.selectProfile(Profile(id = 1L))
        selectedProfileRepository.selectProfile(Profile(id = 2L))
        assertEquals(listOf(0L, 1L, 2L), selectedProfiles)
    }
}