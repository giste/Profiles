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

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import de.mannodermaus.junit5.compose.createComposeExtension
import org.giste.profiles.R
import org.giste.profiles.domain.BooleanSetting
import org.giste.profiles.domain.Profile
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class ProfileScreenInstrumentedTests {
    @OptIn(ExperimentalTestApi::class)
    @RegisterExtension
    @JvmField
    val extension = createComposeExtension()

    @Test
    fun brightness_level_is_disabled_when_auto_brightness_is_on() {
        var brightnessLevelApplyContentDescription = ""

        extension.use {
            setContent {
                brightnessLevelApplyContentDescription = stringResource(
                    R.string.profile_screen_brightness_level_apply_content_description
                )

                ProfileScreen(
                    uiState = ProfileViewModel.UiState(
                        profile = Profile(
                            name = "Profile name",
                            autoBrightness = BooleanSetting(apply = true, value = true),
                        )
                    ),
                    onValueChange = { }
                )
            }

            onNodeWithContentDescription(brightnessLevelApplyContentDescription)
                .assertIsNotEnabled()
        }
    }

    @Test
    fun brightness_level_is_enabled_when_auto_brightness_is_off() {
        var brightnessLevelApplyContentDescription = ""

        extension.use {
            setContent {
                brightnessLevelApplyContentDescription = stringResource(
                    R.string.profile_screen_brightness_level_apply_content_description
                )

                ProfileScreen(
                    uiState = ProfileViewModel.UiState(
                        profile = Profile(
                            name = "Profile name",
                            autoBrightness = BooleanSetting(apply = false, value = true),
                        )
                    ),
                    onValueChange = { }
                )
            }

            onNodeWithContentDescription(brightnessLevelApplyContentDescription).assertIsEnabled()
        }
    }
}