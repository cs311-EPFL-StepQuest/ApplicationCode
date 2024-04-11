package com.github.se.stepquest.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.github.se.stepquest.ui.theme.StepQuestTheme
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun dummy_is_correctly_displayed() {
    composeTestRule.setContent { StepQuestTheme { HomeScreen() } }
    // Verify that the messages button is displayed
    composeTestRule.onNodeWithTag("messages_button").assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("messages_icon").assertIsDisplayed()

    // Verify that the notification button is displayed
    composeTestRule.onNodeWithTag("notifications_button").assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("notifications_icon").assertIsDisplayed()

    // Verify that the profile button is displayed
    composeTestRule.onNodeWithTag("profile_button").assertIsDisplayed()

    composeTestRule.onNodeWithContentDescription("profile_icon").assertIsDisplayed()

    // Verify that content of the "Start Game" button is displayed
    composeTestRule.onNodeWithText("Start Game").assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("play_icon").assertIsDisplayed()

    // Verify that the "Challenges" card is displayed
    composeTestRule.onNodeWithText("Challenges").assertIsDisplayed()
    composeTestRule.onNodeWithText("Accept").assertIsDisplayed()
    composeTestRule.onNodeWithText("Reject").assertIsDisplayed()

    // Verify that the "Daily Quests" card is displayed
    composeTestRule.onNodeWithText("Daily Quests").assertIsDisplayed()
  }
}
