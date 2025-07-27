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
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import de.mannodermaus.junit5.compose.createComposeExtension
import org.giste.profiles.R
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NewProfileDialogInstrumentedTests {
    @OptIn(ExperimentalTestApi::class)
    @RegisterExtension
    @JvmField
    val extension = createComposeExtension()

    @Test
    fun name_is_sent_to_view_model_after_debounce() {
        val namesChanged = mutableListOf<String>()
        var nameText = ""

        extension.use {
            setContent {
                nameText = stringResource(R.string.profile_name_dialog_label)
                NewProfileDialog(
                    uiState = NewProfileViewModel.UiState(),
                    onNameChange = { namesChanged.add(it) },
                    onAccept = { },
                    onDismiss = { },
                    onNewProfile = { },
                )
            }

            onNodeWithText(nameText).performTextInput("P")
            waitForIdle()
            onNodeWithText("P").performTextInput("1")
            waitForIdle()

            // Text field has the right text
            onNodeWithText("P1").assertExists()
            mainClock.autoAdvance = false
            // No name change is invoked...
            assertEquals(emptyList<String>(), namesChanged)
            // ...until debounce takes effect
            mainClock.advanceTimeBy(600L)
            assertEquals(listOf("P1"), namesChanged)
        }
    }

}