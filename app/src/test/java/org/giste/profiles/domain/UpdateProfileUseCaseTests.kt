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
        private val PROFILE_1 = Profile(name = "Profile 1")
    }

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var profileRepository: ProfileRepository

    private lateinit var useCase: AddProfileUseCase

    @Before
    fun setUp() {
        useCase = AddProfileUseCase(profileRepository)
    }

    @Test
    fun addProfile_noExceptionIsThrown_returnsNewId() = runTest {
        coEvery { profileRepository.add(PROFILE_1) } returns 1

        val newId = useCase.invoke(PROFILE_1)

        assertThat(newId, equalTo(1))
        coVerify(exactly = 1) { profileRepository.add(PROFILE_1) }
    }

    @Test(expected = SQLiteConstraintException::class)
    fun addProfile_exceptionIsThrown_throwsException() = runTest {
        coEvery { profileRepository.add(PROFILE_1) } throws SQLiteConstraintException()

        useCase.invoke(PROFILE_1)
    }
}