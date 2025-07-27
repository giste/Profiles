/*
 * Copyright 2025 Giste Trappiste
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.giste.profiles.ui

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.SystemProperties
import org.giste.profiles.domain.usecases.FindProfileByIdUseCase
import org.giste.profiles.domain.usecases.FindSystemPropertiesUseCase
import org.giste.profiles.domain.usecases.UpdateProfileUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileViewModelTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val findProfileByIdUseCase = mockk<FindProfileByIdUseCase>()
    private val findSystemPropertiesUseCase = mockk<FindSystemPropertiesUseCase>()
    private val updateProfileUseCase = mockk<UpdateProfileUseCase>()
    private val profileByIdFlow = MutableSharedFlow<Profile>()
    private val systemPropertiesFlow = MutableStateFlow(SystemProperties())
    private lateinit var profileViewModel: ProfileViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        clearAllMocks()
        coEvery { findSystemPropertiesUseCase() } returns systemPropertiesFlow
        coEvery { findProfileByIdUseCase(any()) } returns profileByIdFlow
        profileViewModel = ProfileViewModel(
            profileId = 1L,
            updateProfileUseCase = updateProfileUseCase,
            findProfileByIdUseCase = findProfileByIdUseCase,
            findSystemPropertiesUseCase = findSystemPropertiesUseCase,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun state_changes_when_profile_changes() = runTest(dispatcher) {
        // Assert initial state
        assertEquals(
            ProfileViewModel.UiState(Profile(), SystemProperties()),
            profileViewModel.uiState.value,
        )

        backgroundScope.launch {
            profileViewModel.uiState.collect { println("Collected $it") }
        }

        val newProfile = Profile(name = "New profile")
        profileByIdFlow.emit(newProfile)
        assertEquals(
            ProfileViewModel.UiState(newProfile, SystemProperties()),
            profileViewModel.uiState.value,
        )
    }
}