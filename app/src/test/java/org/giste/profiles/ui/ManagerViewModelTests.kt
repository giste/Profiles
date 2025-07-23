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
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.usecases.DeleteProfileUseCase
import org.giste.profiles.domain.usecases.FindAllProfilesUseCase
import org.giste.profiles.domain.usecases.FindSelectedProfileUseCase
import org.giste.profiles.domain.usecases.SelectProfileUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManagerViewModelTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val findAllProfilesUseCase = mockk<FindAllProfilesUseCase>()
    private val deleteProfileUseCase = mockk<DeleteProfileUseCase>()
    private val findSelectedProfileUseCase = mockk<FindSelectedProfileUseCase>()
    private val selectProfileUseCase = mockk<SelectProfileUseCase>()
    private val profilesFlow = MutableSharedFlow<List<Profile>>()
    private val selectedProfileFlow = MutableSharedFlow<Long>()
    private lateinit var managerViewModel: ManagerViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        clearAllMocks()
        coEvery { findAllProfilesUseCase() } returns profilesFlow
        coEvery { findSelectedProfileUseCase() } returns selectedProfileFlow
        managerViewModel = ManagerViewModel(
            findAllProfilesUseCase = findAllProfilesUseCase,
            deleteProfileUseCase = deleteProfileUseCase,
            findSelectedProfileUseCase = findSelectedProfileUseCase,
            selectProfileUseCase = selectProfileUseCase,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun profiles_are_updated_when_new_values_are_available() = runTest(dispatcher) {
        val profile1 = Profile(1, "Profile 1")
        val profile2 = Profile(2, "Profile 2")

        assertEquals(ManagerViewModel.UiState(), managerViewModel.uiState.value)

        backgroundScope.launch {
            managerViewModel.uiState.collect { println("Collected $it") }
        }

        // Emit initial values
        profilesFlow.emit(emptyList())
        selectedProfileFlow.emit(0L)
        // Emit new values
        profilesFlow.emit(listOf(profile1, profile2))
        selectedProfileFlow.emit(2L)
        assertEquals(
            ManagerViewModel.UiState(profiles = listOf(profile1, profile2), selectedProfile = 2L),
            managerViewModel.uiState.value,
        )
    }

    @Test
    fun repository_is_invoked_when_selecting_profile() = runTest {
        val profile = Profile(1L, "Profile 1")
        coEvery { selectProfileUseCase(profile) } returns Unit

        managerViewModel.onUiAction(ManagerViewModel.UiAction.SelectProfile(profile))

        coVerify { selectProfileUseCase(profile) }
    }

    @Test
    fun repository_is_invoked_when_deleting_profile() = runTest {
        val profile = Profile(1L, "Profile 1")
        coEvery { deleteProfileUseCase(profile) } returns Unit

        managerViewModel.onUiAction(ManagerViewModel.UiAction.DeleteProfile(profile))

        coVerify { deleteProfileUseCase(profile) }
    }
}