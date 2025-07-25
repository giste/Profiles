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

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileNotFoundException
import org.giste.profiles.domain.repositories.ProfileRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DaoProfileRepositoryTests {
    private val profileEntity1 = ProfileEntity(1, "Profile 1")
    private val profileEntity2 = ProfileEntity(2, "Profile 2")
    private val profileEntity3 = ProfileEntity(3, "Profile 3")

    private val profile1 = Profile(1, "Profile 1")
    private val profile2 = Profile(2, "Profile 2")
    private val profile3 = Profile(3, "Profile 3")

    private val profileDao = mockk<ProfileDao>()
    private val profileRepository: ProfileRepository = DaoProfileRepository(profileDao)

    @BeforeEach
    fun beforeEach() {
        clearAllMocks()
    }

    @Test
    fun findAll_returns_all_existing_profiles() = runTest {
        val entities = listOf(profileEntity1, profileEntity2, profileEntity3)
        coEvery { profileDao.findAll() } returns flowOf(entities)
        val expectedProfiles = listOf(profile1, profile2, profile3)

        val actualProfiles = profileRepository.findAll().first()

        assertEquals(expectedProfiles, actualProfiles)
    }

    @Test
    fun findById_returns_profile_if_it_exists() = runTest {
        coEvery { profileDao.findById(1) } returns flowOf(profileEntity1)

        val actualProfile = profileRepository.findById(1).first()

        assertEquals(profile1, actualProfile)
    }

    @Test
    fun findById_throws_exception_if_it_does_not_exist() {
        coEvery { profileDao.findById(4) } returns flowOf(null)
        assertThrows(ProfileNotFoundException::class.java) {
            runTest {
                profileRepository.findById(4).first()
            }
        }
    }

    @Test
    fun add_returns_id_if_profile_does_not_exists() = runTest {
        val profile = Profile(name = "New profile")
        val profileEntity = ProfileEntity(name = "New profile")
        coEvery { profileDao.add(profileEntity) } returns 1L

        val actualId = profileRepository.add(profile)

        assertEquals(1L, actualId)
    }

    @Test
    fun updates_profile_if_it_exists() = runTest {
        val profile = Profile(1, "Updated profile")
        val profileEntity = ProfileEntity(1, "Updated profile")
        coEvery { profileDao.update(profileEntity) } returns 1

        profileRepository.update(profile)
    }

    @Test
    fun update_throws_exception_if_profile_does_not_exist() {
        val profile = Profile(name = "Updated profile")
        val profileEntity = ProfileEntity(name = "Updated profile")
        coEvery { profileDao.update(profileEntity) } returns 0

        assertThrows(ProfileNotFoundException::class.java) {
            runTest {
                profileRepository.update(profile)
            }
        }
    }

    @Test
    fun deletes_profile_if_it_exists() = runTest {
        val profile = Profile(1L, "Profile to delete")
        val profileEntity = ProfileEntity(1L, "Profile to delete")
        coEvery { profileDao.delete(profileEntity) } returns 1

        profileRepository.delete(profile)
    }

    @Test
    fun delete_throws_exception_if_profile_does_not_exist() {
        val profile = Profile(name = "Profile to delete")
        val profileEntity = ProfileEntity(name = "Profile to delete")
        coEvery { profileDao.delete(profileEntity) } returns 0

        assertThrows(ProfileNotFoundException::class.java) {
            runTest {
                profileRepository.delete(profile)
            }
        }
    }

    @Test
    fun name_exists_returns_true_if_name_exists() = runTest {
        coEvery { profileDao.findByName("Existing name") } returns ProfileEntity(name = "Existing name")

        val nameExists = profileRepository.nameExists("Existing name")

        assertTrue(nameExists)
    }

    @Test
    fun name_exists_returns_false_if_name_does_not_exist() = runTest {
        coEvery { profileDao.findByName("Non existing name") } returns null

        val nameExists = profileRepository.nameExists("Non existing name")

        assertFalse(nameExists)
    }
}