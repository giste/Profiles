package org.giste.profiles.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SelectedProfileDaoTests {
    private lateinit var selectedProfileDao: SelectedProfileDao
    private lateinit var db: ProfilesDb

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ProfilesDb::class.java).build()
        selectedProfileDao = db.selectedProfileDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun findSelected_noProfileSelected_returnsNull() = runBlocking {
        val selectedProfileEntity = selectedProfileDao.findSelected().first()

        assertThat(selectedProfileEntity, equalTo(null))
    }

    @Test
    fun findSelected_thereIsSelectedProfile_returnsProfileId() = runBlocking {
        selectedProfileDao.selectProfile(SelectedProfileEntity(1))
        val selectedId = selectedProfileDao.findSelected().first()?.selectedId

        assertThat(selectedId, equalTo(1))
    }

    @Test
    fun selectProfile_noSelectedProfile_selectsProfile() = runBlocking {
        selectedProfileDao.selectProfile(SelectedProfileEntity(1))
        val selectedProfileId = selectedProfileDao.findSelected().first()?.selectedId

        assertThat(selectedProfileId, equalTo(1L))
    }

    @Test
    fun selectProfile_otherProfileSelected_selectsProfile() = runBlocking {
        selectedProfileDao.insert(SelectedProfileEntity(2))

        selectedProfileDao.selectProfile(SelectedProfileEntity(1))
        val selectedProfileId = selectedProfileDao.findSelected().first()?.selectedId

        assertThat(selectedProfileId, equalTo(1L))
    }
}