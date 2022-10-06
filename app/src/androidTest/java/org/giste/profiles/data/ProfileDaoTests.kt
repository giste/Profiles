package org.giste.profiles.data

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class ProfileDaoTests {
    private lateinit var profileDao: ProfileDao
    private lateinit var db: ProfilesDb

    private val profile1 = ProfileEntity(id = 1, name = "Profile 1")
    private val profile2 = ProfileEntity(id = 2, name = "Profile 2")
    private val profile3 = ProfileEntity(id = 3, name = "Profile 3")

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ProfilesDb::class.java).build()
        profileDao = db.profileDao()

        runBlocking {
            profileDao.add(profile1)
            profileDao.add(profile2)
            profileDao.add(profile3)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun findAll_profilesExist_returnsProfiles() = runBlocking {
        val profileList = profileDao.findAll().first()

        assertThat(profileList.size, equalTo(3))
        assertThat(profileList[0], equalTo(profile1))
        assertThat(profileList[1], equalTo(profile2))
        assertThat(profileList[2], equalTo(profile3))
    }

    @Test
    fun findById_profileExists_returnsProfile() = runBlocking {
        val profile = profileDao.findById(1).first()

        assertThat(profile, equalTo(profile1))
    }

    @Test
    fun findById_profileDoesNotExist_returnsNull() = runBlocking {
        val profile = profileDao.findById(4).first()

        assertThat(profile, equalTo(null))
    }

    @Test
    fun add_profileIdIsZero_profileIsAdded() = runBlocking {
        val profile = ProfileEntity(name = "New profile")
        val newId = profileDao.add(profile)
        val readProfile = profileDao.findById(newId).first()

        assertThat(readProfile?.name, equalTo(profile.name))
    }

    @Test
    fun add_profileIdDoesNotExist_profileIsAdded() = runBlocking {
        val profile = ProfileEntity(id = 4, name = "Profile 4")
        val newId = profileDao.add(profile)
        val profileList = profileDao.findAll().first()

        assertThat(profileList.size, equalTo(4))
        assertThat(newId, equalTo(4))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun add_profileIdExists_exceptionIsThrown() {
        val profile = ProfileEntity(id = 1, name = "New profile")

        runBlocking {
            profileDao.add(profile)
        }
    }

    @Test
    fun update_profileExists_profileIsUpdated() = runBlocking {
        val profile = ProfileEntity(id = 1, name = "Updated profile")
        val updated = profileDao.update(profile)
        val updatedProfile = profileDao.findById(1).first()

        assertThat(updated, equalTo(1))
        assertThat(updatedProfile, equalTo(profile))
    }

    @Test
    fun update_profileDoesNotExist_doNothing() = runBlocking {
        val profile = ProfileEntity(id = 4, name = "Updated profile")
        val updated = profileDao.update(profile)
        val profileList = profileDao.findAll().first()

        assertThat(updated, equalTo(0))
        assertThat(profileList.size, equalTo(3))
        assertThat(profileList[0], equalTo(profile1))
        assertThat(profileList[1], equalTo(profile2))
        assertThat(profileList[2], equalTo(profile3))
    }

    @Test
    fun delete_profileExists_deletesProfileAndSettings() = runBlocking {
        val profile = ProfileEntity(id = 1)
        profileDao.delete(profile)
        val profileList = profileDao.findAll().first()

        assertThat(profileList.size, equalTo(2))
    }

    @Test
    fun delete_profileDoesNotExist_doesNothing() = runBlocking {
        val profile = ProfileEntity(id = 5)
        profileDao.delete(profile)
        val profileList = profileDao.findAll().first()

        assertThat(profileList.size, equalTo(3))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun add_profileNameExists_throwException(): Unit = runBlocking {
        val newProfile = ProfileEntity(name = "profile 1")
        profileDao.add(newProfile)
    }

    @Test
    fun findByName_nameDoesNotExist_returnNull() = runBlocking {
        val profile = profileDao.findByName("profile 4")

        assertThat(profile, equalTo(null))
    }

    @Test
    fun findByName_nameExists_returnProfile() = runBlocking {
        val profile = profileDao.findByName("profile 2")

        assertThat(profile, equalTo(profile2))
    }
}