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
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NewProfileViewModelTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val profileRepository = mockk<ProfileRepository>()
    private val viewModel = NewProfileViewModel(
        profileRepository = profileRepository,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        clearAllMocks()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun state_changes_when_name_changes() {
        coEvery { profileRepository.nameExists("New name") } returns false

        viewModel.onUiAction(NewProfileViewModel.UiAction.OnNameChange("New name"))

        val expectedState = NewProfileViewModel.UiState(
            name = "New name",
            error = NewProfileViewModel.NameError.NoError,
            id = 0L,
        )
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun state_has_blank_error_when_name_is_blank() {
        coEvery { profileRepository.nameExists("Name") } returns false
        // Set valid name
        viewModel.onUiAction(NewProfileViewModel.UiAction.OnNameChange("Name"))

        viewModel.onUiAction(NewProfileViewModel.UiAction.OnNameChange("  "))

        val expectedState = NewProfileViewModel.UiState(
            name = "  ",
            error = NewProfileViewModel.NameError.BlankNameError,
            id = 0L,
        )
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun state_clears_blank_error_when_name_is_valid() {
        coEvery { profileRepository.nameExists("Valid name") } returns false
        // Set blank name
        viewModel.onUiAction(NewProfileViewModel.UiAction.OnNameChange(""))
        // Check blank error
        val expectedErrorState = NewProfileViewModel.UiState(
            name = "",
            error = NewProfileViewModel.NameError.BlankNameError,
            id = 0L,
        )
        assertEquals(expectedErrorState,viewModel.uiState.value)

        viewModel.onUiAction(NewProfileViewModel.UiAction.OnNameChange("Valid name"))

        val expectedState = NewProfileViewModel.UiState(
            name = "Valid name",
            error = NewProfileViewModel.NameError.NoError,
            id = 0L,
        )
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun state_has_name_exists_error_when_repository_finds_it() {
        coEvery { profileRepository.nameExists("Existing name") } returns true

        viewModel.onUiAction(NewProfileViewModel.UiAction.OnNameChange("Existing name"))

        val expectedState = NewProfileViewModel.UiState(
            name = "Existing name",
            error = NewProfileViewModel.NameError.NameExistsError,
            id = 0L,
        )
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun state_clears_name_exists_error_when_repository_does_not_find_it() {
        coEvery { profileRepository.nameExists("Existing name") } returns true
        coEvery { profileRepository.nameExists("Non existing name") } returns false
        val expectedErrorState = NewProfileViewModel.UiState(
            name = "Existing name",
            error = NewProfileViewModel.NameError.NameExistsError,
            id = 0L,
        )
        // Set name exists error and check
        viewModel.onUiAction(NewProfileViewModel.UiAction.OnNameChange("Existing name"))
        assertEquals(expectedErrorState, viewModel.uiState.value)

        viewModel.onUiAction(NewProfileViewModel.UiAction.OnNameChange("Non existing name"))

        val expectedState = NewProfileViewModel.UiState(
            name = "Non existing name",
            error = NewProfileViewModel.NameError.NoError,
            id = 0L,
        )
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun state_has_new_id_when_repository_adds_it() {
        coEvery { profileRepository.nameExists("Name") } returns false
        coEvery { profileRepository.add(Profile(name = "Name")) } returns 1L
        // Set valid name
        viewModel.onUiAction(NewProfileViewModel.UiAction.OnNameChange("Name"))

        viewModel.onUiAction(NewProfileViewModel.UiAction.OnAccept)

        val expectedState = NewProfileViewModel.UiState(
            name = "Name",
            error = NewProfileViewModel.NameError.NoError,
            id = 1L,
        )
        assertEquals(expectedState, viewModel.uiState.value)
    }
}