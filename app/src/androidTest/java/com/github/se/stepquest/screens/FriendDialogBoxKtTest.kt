package com.github.se.stepquest.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FriendDialogBoxKtTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun friendDialogBoxTest() {
    /*
    val friend = Friend(name = "John Doe", profilePicture = null, status = true)

    composeTestRule.setContent { FriendDialogBox(friend = friend, onDismiss = {}) }

    // Verify the presence of UI elements
    composeTestRule.onNodeWithText("John Doe").assertExists()
    composeTestRule.onNodeWithContentDescription("Close").assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("Profile Picture").assertDoesNotExist()
    composeTestRule.onNodeWithText("Connect").assertExists()
    composeTestRule.onNodeWithText("Challenge").assertExists()

    // Perform click on Connect button
    composeTestRule.onNodeWithText("Connect").performClick()

    // Perform click on Challenge button
    composeTestRule.onNodeWithText("Challenge").performClick()
    */
  }
}
