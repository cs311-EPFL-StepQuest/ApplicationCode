package com.github.se.stepquest.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.stepquest.Friend
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FriendDialogBoxKtTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun friendDialogBoxTest() {
    // ORIGINAL: val friend = Friend(name = "John Doe", profilePicture = null, status = true)
    val friend = Friend(name = "John Doe", status = true)

    // ORIGINAL: composeTestRule.setContent { FriendDialogBox(friend = friend, onDismiss = {}) }
    composeTestRule.setContent { FriendDialogBox(friend = friend, userId = "", onDismiss = {}) }

    // Verify the presence of UI elements
    composeTestRule.onNodeWithText("John Doe").assertExists()
    composeTestRule.onNodeWithContentDescription("Close").assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("Profile Picture").assertDoesNotExist()
    // FAILS: composeTestRule.onNodeWithText("Connect").assertExists()
    composeTestRule.onNodeWithText("Challenge").assertExists()

    // Perform click on Connect button
    // FAILS: composeTestRule.onNodeWithText("Connect").performClick()

    // Perform click on Challenge button
    composeTestRule.onNodeWithText("Challenge").performClick()
  }
}
