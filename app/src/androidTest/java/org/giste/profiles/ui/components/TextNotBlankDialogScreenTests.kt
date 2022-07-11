package org.giste.profiles.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.giste.profiles.ui.theme.ProfilesTheme
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
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

        composeTestRule.onNodeWithTag("TextField").assertIsDisplayed()
    }

    @Test
    fun changeText_textIsBlank_acceptIsNotEnabledAndErrorIsShown() {
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
        composeTestRule.onNodeWithTag("TextField").performTextReplacement("")
        composeTestRule.onNodeWithTag("AcceptButton").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("ErrorText").assertIsDisplayed()
    }

    @Test
    fun changeText_textIsNotBlank_acceptIsEnabledAndErrorIsNotShown() {
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
        composeTestRule.onNodeWithTag("TextField").performTextReplacement("Text")
        composeTestRule.onNodeWithTag("AcceptButton").assertIsEnabled()
        composeTestRule.onNodeWithTag("ErrorText").assertDoesNotExist()
    }

    @Test
    fun clickAccept_onAcceptIsInvokedWithText() {
        composeTestRule.setContent {
            ProfilesTheme {
                TextNotBlankDialogBody(
                    title = "Title",
                    onAccept = { assertThat(it, equalTo("Text")) },
                    label = "Label",
                    text = "Text"
                )
            }
        }

        composeTestRule.onNodeWithTag("AcceptButton").performClick()
    }
}