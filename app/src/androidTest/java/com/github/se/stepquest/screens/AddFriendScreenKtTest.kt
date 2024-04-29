package com.github.se.stepquest.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class AddFriendScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun addFriendScreen_display_works() {
    // Set up the composable content
    composeTestRule.setContent { AddFriendScreen(onDismiss = { /* Do nothing */}) }

    // Verify the Back button is displayed
    composeTestRule.onNodeWithText("Back").assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("Close").assertIsDisplayed()
    composeTestRule.onNodeWithText("Friends").assertIsDisplayed()
    composeTestRule.onNodeWithText("Search for friends").assertIsDisplayed()
    composeTestRule.onNodeWithText("Enter your friend's username").assertExists()
  }

  @Test
  fun searched_friend_is_displayed() {
    val currentUser = "Current User"
    val name = "Friend Name"
    composeTestRule.setContent { UserItem(currentUser = currentUser, name = name) }
    composeTestRule.onNodeWithText(text = name).assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("Close").assertDoesNotExist()
    composeTestRule.onNodeWithText(text = name).performClick()
    composeTestRule.onNodeWithText("Send a friend request").performClick()
  }
}
