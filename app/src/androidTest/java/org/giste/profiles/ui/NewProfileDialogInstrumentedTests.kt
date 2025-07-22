package org.giste.profiles.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import de.mannodermaus.junit5.compose.createComposeExtension
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

        extension.use {
            setContent {
                NewProfileDialog(
                    uiState = NewProfileViewModel.UiState(),
                    onNameChange = { namesChanged.add(it) },
                    onAccept = { },
                    onDismiss = { },
                    onNewProfile = { },
                )
            }

            onNodeWithTag("NAME_FIELD").performTextInput("P")
            waitForIdle()
            onNodeWithTag("NAME_FIELD").performTextInput("1")
            waitForIdle()

            // Text field has the right text
            onNodeWithTag("NAME_FIELD").assert(hasText("P1"))
            mainClock.autoAdvance = false
            // No name change is invoked...
            assertEquals(emptyList<String>(), namesChanged)
            // ...until debounce takes effect
            mainClock.advanceTimeBy(600L)
            assertEquals(listOf("P1"), namesChanged)
        }
    }

}