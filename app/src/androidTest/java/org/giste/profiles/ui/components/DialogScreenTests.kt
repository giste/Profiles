package org.giste.profiles.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
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
                DialogScreen(
                    title = "Title",
                    cancelLabel = "Cancel",
                    acceptLabel = "Accept",
                    onAccept = { },
                ) {
                    Text("Content")
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
                DialogScreen(
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
                DialogScreen(
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

    @Test
    fun acceptNotEnabled_conditionChangesToTrue_acceptIsEnabled() {
        var enabled by mutableStateOf(false)

        composeTestRule.setContent {
            ProfilesTheme {
                DialogScreen(
                    title = "Title",
                    cancelLabel = "Cancel",
                    acceptLabel = "Accept",
                    onAccept = { },
                    acceptEnabled = { enabled }
                ) {
                    Text("Content")
                }
            }
        }

        composeTestRule.onNodeWithTag("AcceptButton").assertIsNotEnabled()
        enabled = true
        composeTestRule.onNodeWithTag("AcceptButton").assertIsEnabled()
    }

    @Test
    fun acceptEnabled_conditionChangesToFalse_acceptIsNotEnabled() {
        var enabled by mutableStateOf(true)

        composeTestRule.setContent {
            ProfilesTheme {
                DialogScreen(
                    title = "Title",
                    cancelLabel = "Cancel",
                    acceptLabel = "Accept",
                    onAccept = { },
                    acceptEnabled = { enabled }
                ) {
                    Text("Content")
                }
            }
        }

        composeTestRule.onNodeWithTag("AcceptButton").assertIsEnabled()
        enabled = false
        composeTestRule.onNodeWithTag("AcceptButton").assertIsNotEnabled()
    }

}