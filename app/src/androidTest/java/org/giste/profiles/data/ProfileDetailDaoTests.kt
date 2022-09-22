package org.giste.profiles.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.giste.profiles.domain.SettingType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ProfileDetailDaoTests {
    private lateinit var db: ProfilesDb
    private lateinit var profileDetailDao: ProfileDetailDao
    private lateinit var settingDao: SettingDao
    private lateinit var profileDao: ProfileDao

    private val profile1 = ProfileEntity(id = 1, name = "Profile 1")
    private val mediaVolume = SettingEntity(
        id = 1,
        profileId = 1,
        type = SettingType.VOLUME_MEDIA,
        override = false,
        value = 0
    )
    private val ringVolume = SettingEntity(
        id = 2,
        profileId = 1,
        type = SettingType.VOLUME_RING,
        override = false,
        value = 0
    )
    private val notificationVolume = SettingEntity(
        id = 3,
        profileId = 1,
        type = SettingType.VOLUME_NOTIFICATION,
        override = false,
        value = 0
    )
    private val alarmVolume = SettingEntity(
        id = 4,
        profileId = 1,
        type = SettingType.VOLUME_ALARM,
        override = false,
        value = 0
    )

    private val settingList = listOf(mediaVolume, ringVolume, notificationVolume, alarmVolume)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ProfilesDb::class.java).build()
        profileDetailDao = db.profileDetailDao()
        settingDao = db.settingDao()
        profileDao = db.profileDao()

        runBlocking {
            profileDetailDao.add(profile1, settingList)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun add_checkSampleDataIsLoaded() = runBlocking {
        val settingList = settingDao.findByProfileId(profile1.id).first()
        val profileList = profileDao.findAll().first()

        assertThat(profileList, contains(profile1))
        assertThat(settingList, contains(mediaVolume, ringVolume, notificationVolume, alarmVolume))
    }

    @Test
    fun update_settingsAreUpdated() = runBlocking {
        val settings = listOf(
            mediaVolume.copy(override = true, value = 1),
            ringVolume.copy(override = true, value = 1),
            notificationVolume.copy(override = true, value = 1),
            alarmVolume.copy(override = true, value = 1),
        )

        profileDetailDao.update(profile1, settings)

        val readSettings = settingDao.findByProfileId(profile1.id).first()
        readSettings.forEach {
            assertThat(it.override, equalTo(true))
            assertThat(it.value, equalTo(1))
        }
    }

    @Test
    fun deleteProfile_settingsAreDeleted() = runBlocking {
        profileDao.delete(profile1)

        val settings = settingDao.findByProfileId(profile1.id).first()

        assertThat(settings.size, equalTo(0))
    }

    @Test
    fun findById_returnsProfileAndSettings() = runBlocking {
        val profileMap = profileDetailDao.findById(profile1.id).first()

        assertThat(profileMap.keys, contains(profile1))
        assertThat(
            profileMap[profile1],
            contains(mediaVolume, ringVolume, notificationVolume, alarmVolume)
        )
    }
}