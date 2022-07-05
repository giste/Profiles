package org.giste.profiles.domain

import android.database.sqlite.SQLiteConstraintException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateProfileUseCaseTests {
    companion object {
        private val PROFILE_1 = Profile(1, "Profile 1")
    }

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var profileRepository: ProfileRepository

    private lateinit var useCase: UpdateProfileUseCase

    @Before
    fun setUp() {
        useCase = UpdateProfileUseCase(profileRepository)
    }

    @Test
    fun updateProfile_profileExists_updateProfile() = runTest {
        coEvery { profileRepository.update(PROFILE_1) } returns 1

        val updated = useCase.invoke(PROFILE_1)

        assertThat(updated, equalTo(1))
        coVerify(exactly = 1) { profileRepository.update(PROFILE_1) }
    }

}