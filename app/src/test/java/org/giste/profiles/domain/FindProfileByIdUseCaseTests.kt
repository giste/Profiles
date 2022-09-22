package org.giste.profiles.domain

import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.giste.profiles.domain.usecases.FindProfileByIdUseCase
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FindProfileByIdUseCaseTests {
    companion object {
        private val PROFILE_1 = ProfileDetail(1, "Profile 1")
    }

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var profileRepository: ProfileRepository

    private lateinit var useCase: FindProfileByIdUseCase

    @Before
    fun setUp() {
        useCase = FindProfileByIdUseCase(profileRepository)
    }

    @Test
    fun findProfileById_profileExists_returnsProfile() = runTest {
        coEvery { profileRepository.findById(1) } returns flow { emit(PROFILE_1) }

        val readProfile = useCase.invoke(1).first()

        assertThat(readProfile, equalTo(PROFILE_1))
    }
}