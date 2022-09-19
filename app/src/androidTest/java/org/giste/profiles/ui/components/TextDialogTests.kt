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
    fun thereIsAnError_ErrorTextIsDisplayedAndAcceptIsNotEnabled() {
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

        composeTestRule.onNodeWithTag("ErrorText").assertTextEquals("Error")
        composeTestRule.onNodeWithTag("AcceptButton").assertIsNotEnabled()
    }

    @Test
    fun thereIsNoError_ErrorTextIsNotDisplayedAndAcceptIsEnabled() {
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

        composeTestRule.onNodeWithTag("ErrorText").assertDoesNotExist()
        composeTestRule.onNodeWithTag("AcceptButton").assertIsEnabled()
    }

}