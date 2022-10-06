package org.giste.profiles.domain

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.giste.profiles.data.*
import org.giste.profiles.domain.usecases.FindProfileByIdUseCase
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FindProfileByIdUseCaseIntegrationTests {
    private lateinit var db: ProfilesDb
    private lateinit var profileDao: ProfileDao
    private lateinit var settingDao: SettingDao

    private var id = 0L


    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ProfilesDb::class.java).build()
        profileDao = db.profileDao()
        settingDao = db.settingDao()

        runBlocking {
            id = profileDao.add(ProfileEntity(name = "Profile 1"))
            settingDao.add(SettingEntity(profileId = id, type = SettingType.VOLUME_MEDIA))
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun findById_thereAreMissingSettings_addsAllSettings() = runBlocking {
        val repository = ProfileRepositoryImpl(
            profileDao = profileDao,
            profileMapper = ProfileMapper(),
            selectedProfileDao = db.selectedProfileDao(),
            profileDetailDao = db.profileDetailDao(),
            profileDetailMapper = ProfileDetailMapper(SettingMapper()),
            settingDao = settingDao,
            settingMapper = SettingMapper()
        )

        val flow = FindProfileByIdUseCase(repository).invoke(id)

        assertThat(flow.first().settings.size, equalTo(1))
        assertThat(flow.first().settings.size, equalTo(SettingType.values().size))
    }
}