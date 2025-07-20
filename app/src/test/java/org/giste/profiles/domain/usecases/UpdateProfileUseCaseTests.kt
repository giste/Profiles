package org.giste.profiles.domain.usecases

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.giste.profiles.domain.BooleanSetting
import org.giste.profiles.domain.IntSetting
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateProfileUseCaseTests {
    private val profileRepository = mockk<ProfileRepository>()
    private val updateProfileUseCase = UpdateProfileUseCase(profileRepository)

    @BeforeEach
    fun beforeEach() {
        clearAllMocks()
    }

    @Test
    fun brightness_level_apply_is_false_when_auto_brightness_is_applied_and_true() = runTest {
        val profile = Profile(
            name = "Profile",
            autoBrightness = BooleanSetting(apply = true, value = true),
            brightness = IntSetting(true, 1)
        )
        coEvery { profileRepository.update(any()) } returns Unit

        updateProfileUseCase(profile)

        val expectedProfile = Profile(
            name = "Profile",
            autoBrightness = BooleanSetting(apply = true, value = true),
            brightness = IntSetting(false, 1)
        )
        coVerify { profileRepository.update(expectedProfile) }
    }
}