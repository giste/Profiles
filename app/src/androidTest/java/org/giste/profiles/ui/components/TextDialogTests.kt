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
                    initialText = "Text",
                    error = "",
                    onValidate = {},
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
                    initialText = "Some text",
                    error = "Error",
                    onValidate = {}
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
                    initialText = "",
                    error = "",
                    onValidate = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("ErrorText").assertDoesNotExist()
        composeTestRule.onNodeWithTag("AcceptButton").assertIsEnabled()
    }

}