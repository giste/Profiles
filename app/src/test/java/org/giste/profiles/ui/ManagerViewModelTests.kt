package org.giste.profiles.ui

import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.giste.profiles.domain.*
import org.giste.profiles.domain.usecases.DeleteProfileUseCase
import org.giste.profiles.domain.usecases.FindProfilesUseCase
import org.giste.profiles.domain.usecases.FindSelectedProfileUseCase
import org.giste.profiles.domain.usecases.SelectProfileUseCase
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ManagerViewModelTests {

    companion object {
        private val PROFILE_1 = Profile(1, "Profile 1")
        private val PROFILE_2 = Profile(2, "Profile 2")
    }

    @get:Rule
    val coroutineScope = MainDispatcherRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var findProfilesUseCase: FindProfilesUseCase

    @MockK
    private lateinit var deleteProfileUseCase: DeleteProfileUseCase

    @MockK
    private lateinit var findSelectedProfileUseCase: FindSelectedProfileUseCase

    @MockK
    private lateinit var selectProfileUseCase: SelectProfileUseCase

    private lateinit var viewModel: ManagerViewModel

    @Test
    fun init_profilesExists_profilesAreLoaded() = runTest {
        coEvery { findProfilesUseCase.invoke() } returns flow { emit(listOf(PROFILE_1, PROFILE_2)) }
        coEvery { findSelectedProfileUseCase.invoke() } returns flow { emit(0L) }

        viewModel = ManagerViewModel(
            findProfilesUseCase = findProfilesUseCase,
            deleteProfileUseCase = deleteProfileUseCase,
            findSelectedProfileUseCase = findSelectedProfileUseCase,
            selectProfileUseCase = selectProfileUseCase
        )

        assertThat(viewModel.profileList.size, equalTo(2))
    }

    @Test
    fun init_selectedProfilesExists_profileIsSelected() = runTest {
        coEvery { findProfilesUseCase.invoke() } returns flow { emit(listOf(PROFILE_1, PROFILE_2)) }
        coEvery { findSelectedProfileUseCase.invoke() } returns flow { emit(2L) }

        viewModel = ManagerViewModel(
            findProfilesUseCase = findProfilesUseCase,
            deleteProfileUseCase = deleteProfileUseCase,
            findSelectedProfileUseCase = findSelectedProfileUseCase,
            selectProfileUseCase = selectProfileUseCase
        )

        assertThat(viewModel.selectedProfileId, equalTo(2))
    }

    @Test
    fun onProfileSelected_selectedProfileIsDistinct_profileIsSelected() = runTest {
        coEvery { findProfilesUseCase.invoke() } returns flow { emit(listOf(PROFILE_1, PROFILE_2)) }
        coEvery { findSelectedProfileUseCase.invoke() } returns flow {
            emit(1L)
            delay(10)
            emit(2L)
        }
        coEvery { selectProfileUseCase.invoke(PROFILE_2) } returns Unit

        viewModel = ManagerViewModel(
            findProfilesUseCase = findProfilesUseCase,
            deleteProfileUseCase = deleteProfileUseCase,
            findSelectedProfileUseCase = findSelectedProfileUseCase,
            selectProfileUseCase = selectProfileUseCase
        )

        viewModel.onProfileSelected(PROFILE_2)
        this.advanceTimeBy(20)

        assertThat(viewModel.selectedProfileId, equalTo(2L))
    }
}