package org.giste.profiles.ui

import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.giste.profiles.domain.FindProfilesUseCase
import org.giste.profiles.domain.Profile
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

    private lateinit var viewModel: ManagerViewModel

    @Test
    fun init_profilesExists_profilesAreLoaded() = runTest {
        coEvery { findProfilesUseCase.invoke() } returns flow { emit(listOf(PROFILE_1, PROFILE_2)) }

        viewModel = ManagerViewModel(findProfilesUseCase)

        assertThat(viewModel.profileList.size, equalTo(2))
    }
}