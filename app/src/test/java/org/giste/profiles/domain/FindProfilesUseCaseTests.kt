package org.giste.profiles.domain

import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FindProfilesUseCaseTests {
    companion object {
        private val PROFILE_1 = Profile(1, "Profile 1")
        private val PROFILE_2 = Profile(2, "Profile 2")
    }

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var profileRepository: ProfileRepository

    private lateinit var useCase: FindProfilesUseCase

    @Before
    fun setUp() {
        useCase = FindProfilesUseCase(profileRepository)
    }

    @Test
    fun findProfiles_returnsProfiles() = runTest {
        coEvery { profileRepository.findAll() } returns flow { emit(listOf(PROFILE_1, PROFILE_2))}

        val profileList = useCase.invoke().first()

        assertThat(profileList, equalTo(listOf(PROFILE_1, PROFILE_2)))
    }
}