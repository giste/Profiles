package org.giste.profiles.ui.components

import androidx.compose.material.Text
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.Espresso
import org.giste.profiles.ui.theme.ProfilesTheme
import org.junit.Rule
import org.junit.Test

class DialogScreenTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dialogIsInvoked_everyPartIsVisible() {
        composeTestRule.setContent {
            ProfilesTheme {
                DialogBody(
                    title = "Title",
                    cancelLabel = "Cancel",
                    acceptLabel = "Accept",
                    onAccept = { }) {
                        Text(text = "Content")
                }
            }
        }

        composeTestRule.onNodeWithTag("Root").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("CancelButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AcceptButton").assertIsDisplayed()
    }

    @Test
    fun cancelIsClicked_dialogIsClosed() {
        composeTestRule.setContent {
            ProfilesTheme {
                DialogBody(
                    title = "Title",
                    cancelLabel = "Cancel",
                    acceptLabel = "Accept",
                    onAccept = { }) {
                    Text(text = "Content")
                }
            }
        }

        composeTestRule.onNodeWithTag("CancelButton").performClick()
        composeTestRule.onNodeWithTag("Root").assertDoesNotExist()
    }

    @Test
    fun acceptIsClicked_dialogIsClosed() {
        composeTestRule.setContent {
            ProfilesTheme {
                DialogBody(
                    title = "Title",
                    cancelLabel = "Cancel",
                    acceptLabel = "Accept",
                    onAccept = { }) {
                    Text(text = "Content")
                }
            }
        }

        composeTestRule.onNodeWithTag("AcceptButton").performClick()
        composeTestRule.onNodeWithTag("Root").assertDoesNotExist()
    }

}