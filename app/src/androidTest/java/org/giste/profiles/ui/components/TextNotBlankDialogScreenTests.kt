package org.giste.profiles.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.giste.profiles.ui.theme.ProfilesTheme
import org.junit.Rule
import org.junit.Test

class TextNotBlankDialogScreenTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun textNotBlankIsInvoked_textFieldIsVisible() {
        composeTestRule.setContent {
            ProfilesTheme {
                TextNotBlankDialogBody(
                    title = "Title",
                    onAccept = { },
                    label = "Label"
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Input Label").assertIsDisplayed()
    }

    @Test
    fun changeText_textIsBlank_acceptIsNotEnabled() {
        composeTestRule.setContent {
            ProfilesTheme {
                TextNotBlankDialogBody(
                    title = "Title",
                    onAccept = { },
                    label = "Label",
                    text = "Initial text"
                )
            }
        }

        composeTestRule.onNodeWithTag("AcceptButton").assertIsEnabled()
        composeTestRule.onNodeWithContentDescription("Input Label").performTextReplacement("")
        composeTestRule.onNodeWithTag("AcceptButton").assertIsNotEnabled()
    }

    @Test
    fun changeText_textIsNotBlank_acceptIsEnabled() {
        composeTestRule.setContent {
            ProfilesTheme {
                TextNotBlankDialogBody(
                    title = "Title",
                    onAccept = { },
                    label = "Label"
                )
            }
        }

        composeTestRule.onNodeWithTag("AcceptButton").assertIsNotEnabled()
        composeTestRule.onNodeWithContentDescription("Input Label").performTextReplacement("Text")
        composeTestRule.onNodeWithTag("AcceptButton").assertIsEnabled()
    }
}