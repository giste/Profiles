package org.giste.profiles.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.giste.profiles.ui.theme.ProfilesTheme
import org.junit.Rule
import org.junit.Test

class TextDialogTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun textDialogIsInvoked_textFieldIsVisible() {
        composeTestRule.setContent {
            ProfilesTheme {
                TextDialogScreen(
                    title = "Title",
                    onAccept = { },
                    label = "Label",
                    text = "Text",
                    error = "",
                    onTextChange = {},
                    maxLength = 20
                )
            }
        }

        composeTestRule.onNodeWithTag("TextField").assertIsDisplayed()
    }

    @Test
    fun onChange_thereIsAnError_ErrorTextIsDisplayedAndAcceptIsNotEnabled() {
        composeTestRule.setContent {
            ProfilesTheme {
                TextDialogScreen(
                    title = "Title",
                    onAccept = { },
                    label = "Label",
                    text = "Some text",
                    error = "Error",
                    onTextChange = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("TextField").performTextReplacement("")
        composeTestRule.onNodeWithTag("ErrorText").assertTextEquals("Can't be empty")
        composeTestRule.onNodeWithTag("AcceptButton").assertIsNotEnabled()
    }

    @Test
    fun onChange_thereIsNoError_ErrorTextIsNotDisplayedAndAcceptIsEnabled() {
        composeTestRule.setContent {
            ProfilesTheme {
                TextDialogScreen(
                    title = "Title",
                    onAccept = { },
                    label = "Label",
                    text = "",
                    error = "",
                    onTextChange = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("TextField").performTextReplacement("Some text")
        composeTestRule.onNodeWithTag("ErrorText").assertDoesNotExist()
        composeTestRule.onNodeWithTag("AcceptButton").assertIsEnabled()
    }

    @Test
    fun onChange_maxLengthIsDefinedAndTextIsLonger_textIsNotUpdated() {
        composeTestRule.setContent {
            ProfilesTheme {
                TextDialogScreen(
                    title = "Title",
                    onAccept = { },
                    label = "Label",
                    text = "1234567890",
                    error = "",
                    onTextChange = {},
                    maxLength = 10
                )
            }
        }

        composeTestRule.onNodeWithTag("TextField").performTextReplacement("12345678901")
        composeTestRule.onNodeWithTag("TextField")
            .assert(hasText(text = "1234567890", substring = false))
        composeTestRule.onNodeWithTag("LengthText", true).assert(hasText("10/10"))
    }
}