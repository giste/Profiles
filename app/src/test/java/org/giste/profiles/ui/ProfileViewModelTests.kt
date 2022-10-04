package org.giste.profiles.ui

import androidx.lifecycle.SavedStateHandle
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.giste.profiles.domain.usecases.FindProfileByIdUseCase
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileDetail
import org.giste.profiles.domain.SystemProperties
import org.giste.profiles.domain.usecases.UpdateProfileUseCase
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTests {
    @get:Rule
    val coroutineScope = MainDispatcherRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var findProfileByIdUseCase: FindProfileByIdUseCase

    @MockK
    private lateinit var updateProfileUseCase: UpdateProfileUseCase

    @MockK
    private lateinit var systemProperties: SystemProperties

    @MockK
    private lateinit var state: SavedStateHandle

    @Test
    fun init_idIsNotZero_profileIsLoaded() = runTest {
        every { state.get<Long>("id") } returns 1
        coEvery { findProfileByIdUseCase.invoke(1) } returns flow {
            emit(ProfileDetail(1,"Profile 1"))
        }

        val profileViewModel = ProfileViewModel(
            findProfileByIdUseCase,
            updateProfileUseCase,
            systemProperties,
            state
        )

        assertThat(profileViewModel.profile, equalTo(ProfileDetail(1,"Profile 1")))
        verify { state.get<Long>("id") }
        coVerify(exactly = 1) {
            findProfileByIdUseCase.invoke(1)
        }
    }
}