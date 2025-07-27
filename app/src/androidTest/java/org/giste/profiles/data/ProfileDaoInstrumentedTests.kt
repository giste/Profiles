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

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.giste.profiles.domain.RingModeSetting
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileDaoInstrumentedTests {
    private val profile1 = ProfileEntity(id = 1, name = "Profile 1")
    private val profile2 = ProfileEntity(id = 2, name = "Profile 2")
    private val profile3 = ProfileEntity(id = 3, name = "Profile 3")

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val db = Room.inMemoryDatabaseBuilder(context, ProfilesDatabase::class.java).build()
    private val profileDao = db.profileDao()

    @BeforeEach
    fun beforeEach() = runTest {
        db.clearAllTables()
        profileDao.add(profile1)
        profileDao.add(profile2)
        profileDao.add(profile3)
    }

    @Test
    fun findAll_returns_all_existing_profiles() = runTest {
        val expectedProfiles = listOf(profile1, profile2, profile3)

        val actualProfiles = profileDao.findAll().first()

        assertEquals(expectedProfiles, actualProfiles)
    }

    @Test
    fun findById_returns_requested_profile_if_it_exists() = runTest {
        val expectedProfile = profile1

        val actualProfile = profileDao.findById(1).first()

        assertEquals(expectedProfile, actualProfile)
    }

    @Test
    fun findById_returns_null_if_requested_profile_does_not_exist() = runTest {
        val actualProfile = profileDao.findById(4).first()

        assertEquals(null, actualProfile)
    }

    @Test
    fun add_adds_new_profile_if_id_is_null() = runTest {
        val newProfile = ProfileEntity(
            name = "New profile",
            applyMediaVolume = true,
            mediaVolume = 1,
            applyRingVolume = true,
            ringVolume = 2,
            applyNotificationVolume = true,
            notificationVolume = 3,
            applyAlarmVolume = true,
            alarmVolume = 4,
            applyAutoBrightness = true,
            autoBrightness = true,
            applyBrightnessLevel = true,
            brightnessLevel = 5,
            applyRingMode = true,
            ringMode = RingModeSetting.Companion.RingMode.VIBRATION,
        )

        val newId = profileDao.add(newProfile)

        val expectedProfile = newProfile.copy(id = newId)
        val actualProfile = profileDao.findById(newId).first()
        assertEquals(expectedProfile, actualProfile)
    }

    @Test
    fun add_adds_new_profile_if_id_does_not_exist() = runTest {
        val expectedProfile = ProfileEntity(
            id = 4,
            name = "Updated profile",
            applyMediaVolume = true,
            mediaVolume = 1,
            applyRingVolume = true,
            ringVolume = 2,
            applyNotificationVolume = true,
            notificationVolume = 3,
            applyAlarmVolume = true,
            alarmVolume = 4,
            applyAutoBrightness = true,
            autoBrightness = true,
            applyBrightnessLevel = true,
            brightnessLevel = 5,
            applyRingMode = true,
            ringMode = RingModeSetting.Companion.RingMode.VIBRATION,
        )

        val newId = profileDao.add(expectedProfile)

        val actualProfile = profileDao.findById(newId).first()
        assertEquals(expectedProfile, actualProfile)
    }

    @Test
    fun add_throws_exception_if_id_exists() {
        val newProfile = ProfileEntity(id = 1, name = "New profile")

        assertThrows(SQLiteConstraintException::class.java) {
            runTest {
                profileDao.add(newProfile)
            }
        }
    }

    @Test
    fun update_updates_profile_if_it_exists() = runTest {
        val expectedProfile = ProfileEntity(
            id = 1,
            name = "Updated profile",
            applyMediaVolume = true,
            mediaVolume = 1,
            applyRingVolume = true,
            ringVolume = 2,
            applyNotificationVolume = true,
            notificationVolume = 3,
            applyAlarmVolume = true,
            alarmVolume = 4,
            applyAutoBrightness = true,
            autoBrightness = true,
            applyBrightnessLevel = true,
            brightnessLevel = 5,
            applyRingMode = true,
            ringMode = RingModeSetting.Companion.RingMode.VIBRATION,
        )

        val updated = profileDao.update(expectedProfile)

        val actualProfile = profileDao.findById(1).first()
        assertEquals(1, updated)
        assertEquals(expectedProfile, actualProfile)
    }

    @Test
    fun update_does_nothing_if_profile_does_not_exist() = runTest {
        val updatedProfile = ProfileEntity(id = 4, name = "Updated profile")
        val expectedProfiles = listOf(profile1, profile2, profile3)

        val updated = profileDao.update(updatedProfile)

        val actualProfiles = profileDao.findAll().first()
        assertEquals(0, updated)
        assertEquals(expectedProfiles, actualProfiles)
    }

    @Test
    fun delete_deletes_profile_if_it_exists() = runTest {
        val profile = ProfileEntity(id = 1)
        val expectedProfiles = listOf(profile2, profile3)

        profileDao.delete(profile)

        val actualProfiles = profileDao.findAll().first()
        assertEquals(expectedProfiles, actualProfiles)
    }

    @Test
    fun delete_does_nothing_if_profile_does_not_exist() = runTest {
        val profile = ProfileEntity(id = 5)
        val expectedProfiles = listOf(profile1, profile2, profile3)

        profileDao.delete(profile)

        val actualProfiles = profileDao.findAll().first()
        assertEquals(expectedProfiles, actualProfiles)
    }

    @Test
    fun add_throws_exception_if_profile_name_exists() {
        val newProfile = ProfileEntity(name = "profile 1")

        assertThrows(SQLiteConstraintException::class.java) {
            runTest {
                profileDao.add(newProfile)
            }
        }
    }

    @Test
    fun findByName_nameExists_returnProfile() = runTest {
        val expectedProfile = profile2

        val actualProfile = profileDao.findByName("profile 2")

        assertEquals(expectedProfile, actualProfile)
    }

    @Test
    fun findByName_returns_null_if_name_does_not_exist() = runTest {
        val actualProfile = profileDao.findByName("profile 4")

        assertEquals(null, actualProfile)
    }

}