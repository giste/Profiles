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
    fun textNotBlankIsInvoked_textFieldIsVisible() {
        composeTestRule.setContent {
            ProfilesTheme {
                TextDialog(
                    title = "Title",
                    onAccept = { },
                    label = "Label"
                )
            }
        }

        composeTestRule.onNodeWithTag("TextField").assertIsDisplayed()
    }

    @Test
    fun onChange_thereIsAnError_ErrorTextIsDisplayedAndAcceptIsNotEnabled() {
        composeTestRule.setContent {
            ProfilesTheme {
                TextDialog(
                    title = "Title",
                    onAccept = { },
                    label = "Label",
                    initialText = "Some text",
                    getErrorForText = {
                        if (it.isBlank()) {
                            "Can't be empty"
                        } else {
                            ""
                        }
                    }
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
                TextDialog(
                    title = "Title",
                    onAccept = { },
                    label = "Label",
                    initialText = "",
                    getErrorForText = {
                        if (it.isBlank()) {
                            "Can't be empty"
                        } else {
                            ""
                        }
                    }
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
                TextDialog(
                    title = "Title",
                    onAccept = { },
                    label = "Label",
                    initialText = "1234567890",
                    maxLength = 10
                )
            }
        }

        composeTestRule.onNodeWithTag("TextField").performTextReplacement("12345678901")
        composeTestRule.onNodeWithTag("TextField").assert(hasText(text = "1234567890", substring = false))
        composeTestRule.onNodeWithTag("LengthText", true).assert(hasText("10/10"))
    }
}