package org.giste.profiles.data

import android.database.sqlite.SQLiteConstraintException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileDetail
import org.giste.profiles.domain.ProfileRepository
import org.giste.profiles.domain.SettingType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileRepositoryTests {
    companion object {
        private val PROFILE_1 = Profile(1, "Profile 1")
        private val PROFILE_2 = Profile(2, "Profile 2")

        private val PROFILE_DETAIL_1 = ProfileDetail(1, "Profile 1")

        private val PROFILE_ENTITY_1 = ProfileEntity(1, "Profile 1")
        private val PROFILE_ENTITY_2 = ProfileEntity(2, "Profile 2")

        private val SETTING_ENTITY_LIST = listOf(
            SettingEntity(1, SettingType.VOLUME_MEDIA, false, 0),
            SettingEntity(1, SettingType.VOLUME_RING, false, 0),
            SettingEntity(1, SettingType.VOLUME_NOTIFICATION, false, 0),
            SettingEntity(1, SettingType.VOLUME_ALARM, false, 0)
        )
    }

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var profileDao: ProfileDao

    @MockK
    private lateinit var selectedProfileDao: SelectedProfileDao

    @MockK
    private lateinit var profileDetailDao: ProfileDetailDao

    private val profileMapper = ProfileMapper()
    private val profileDetailMapper = ProfileDetailMapper(SettingMapper())

    private lateinit var repository: ProfileRepository

    @Before
    fun setUp() {
        repository = ProfileRepositoryImpl(
            profileDao = profileDao,
            profileMapper = profileMapper,
            selectedProfileDao = selectedProfileDao,
            profileDetailDao = profileDetailDao,
            profileDetailMapper = profileDetailMapper
        )
    }

    @Test
    fun findAll_thereAreProfiles_returnsProfileList() = runTest {
        every { profileDao.findAll() } returns flow {
            emit(
                listOf(
                    PROFILE_ENTITY_1,
                    PROFILE_ENTITY_2
                )
            )
        }

        val readProfiles = repository.findAll().first()

        assertThat(readProfiles, equalTo(listOf(PROFILE_1, PROFILE_2)))
        verify(exactly = 1) { profileDao.findAll() }
    }

    @Test
    fun findAll_thereIsNoProfile_returnsEmptyList() = runTest {
        every { profileDao.findAll() } returns flow { emit(listOf()) }

        val readProfiles = repository.findAll().first()

        assertThat(readProfiles, equalTo(listOf()))
        verify(exactly = 1) { profileDao.findAll() }
    }

    @Test
    fun findById_profileExist_returnsProfile() = runTest {
        every { profileDetailDao.findById(1) } returns flow {
            emit(mapOf(PROFILE_ENTITY_1 to SETTING_ENTITY_LIST))
        }

        val readProfile = repository.findById(1).first()

        assertThat(readProfile, equalTo(PROFILE_DETAIL_1))
        verify(exactly = 1) { profileDetailDao.findById(1) }
    }

    @Test(expected = NoSuchElementException::class)
    fun findById_profileDoesNotExist_throwsException() = runTest {
        every { profileDetailDao.findById(1) } returns flow { emit(mapOf()) }

        repository.findById(1).first()
    }

    @Test
    fun add_profileIdDoesNotExist_returnsId() = runTest {
        val newProfile = ProfileDetail(name = "New profile")
        val newProfileEntity = ProfileEntity(name = "New profile")
        coEvery {
            profileDetailDao.add(
                newProfileEntity, listOf(
                    SettingEntity(0, SettingType.VOLUME_MEDIA, false, 0),
                    SettingEntity(0, SettingType.VOLUME_RING, false, 0),
                    SettingEntity(0, SettingType.VOLUME_NOTIFICATION, false, 0),
                    SettingEntity(0, SettingType.VOLUME_ALARM, false, 0)
                )
            )
        } returns 1

        val id = repository.add(newProfile)

        assertThat(id, equalTo(1))
        coVerify(exactly = 1) {
            profileDetailDao.add(
                newProfileEntity, listOf(
                    SettingEntity(0, SettingType.VOLUME_MEDIA, false, 0),
                    SettingEntity(0, SettingType.VOLUME_RING, false, 0),
                    SettingEntity(0, SettingType.VOLUME_NOTIFICATION, false, 0),
                    SettingEntity(0, SettingType.VOLUME_ALARM, false, 0)
                )
            )
        }
    }

    @Test(expected = SQLiteConstraintException::class)
    fun add_profileIdExists_throwsException() = runTest {
        coEvery { profileDetailDao.add(PROFILE_ENTITY_1, SETTING_ENTITY_LIST) } throws SQLiteConstraintException()

        repository.add(PROFILE_DETAIL_1)
    }

    @Test
    fun update_profileExists_updatesProfile() = runTest {
        coEvery { profileDetailDao.update(PROFILE_ENTITY_1, SETTING_ENTITY_LIST) } returns 1

        val updated = repository.update(PROFILE_DETAIL_1)

        assertThat(updated, equalTo(1))
        coVerify(exactly = 1) { profileDetailDao.update(PROFILE_ENTITY_1, SETTING_ENTITY_LIST) }
    }

    @Test
    fun delete_profileExists_deletesProfile() = runTest {
        coEvery { profileDao.delete(PROFILE_ENTITY_1) } returns Unit

        repository.delete(PROFILE_1)

        coVerify(exactly = 1) { profileDao.delete(PROFILE_ENTITY_1) }
    }

    @Test
    fun findSelected_noProfileSelected_returnsZero() = runTest {
        coEvery { selectedProfileDao.findSelected() } returns flow { emit(null) }

        val id = repository.findSelectedProfile().first()
        assertThat(id, equalTo(0L))

        coVerify(exactly = 1) { selectedProfileDao.findSelected() }
    }

    @Test
    fun findSelected_thereIsSelectedProfile_returnsId() = runTest {
        coEvery { selectedProfileDao.findSelected() } returns flow { emit(SelectedProfileEntity(1L)) }

        val id = repository.findSelectedProfile().first()
        assertThat(id, equalTo(1L))

        coVerify(exactly = 1) { selectedProfileDao.findSelected() }
    }

    @Test
    fun selectProfile_daoIsInvoked() = runTest {
        coEvery { selectedProfileDao.selectProfile(SelectedProfileEntity(1L)) } returns Unit

        repository.selectProfile(PROFILE_1)

        coVerify(exactly = 1) { selectedProfileDao.selectProfile(SelectedProfileEntity(1L)) }
    }

    @Test
    fun checkIfExists_profileDoesNotExist_returnFalse() = runTest {
        coEvery { profileDao.findByName("profile 1") } returns null

        val exists = repository.checkIfExists("profile 1")

        assertThat(exists, equalTo(false))
        coVerify { profileDao.findByName("profile 1") }
    }

    @Test
    fun checkIfExists_profileExists_returnTrue() = runTest {
        coEvery { profileDao.findByName("profile 1") } returns ProfileEntity(1L, "Profile 1")

        val exists = repository.checkIfExists("profile 1")

        assertThat(exists, equalTo(true))
        coVerify { profileDao.findByName("profile 1") }
    }
}