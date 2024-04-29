package com.github.se.stepquest

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.github.se.stepquest.ui.theme.StepQuestTheme
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class ProfilePageLayoutTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun display_profile_layout() {
    composeTestRule.setContent {
      StepQuestTheme { ProfilePageLayout(navigationActions = mockk(relaxed = true)) }
    }

    // Assertions
    // Check if the "Settings" image is displayed
    composeTestRule.onNodeWithContentDescription("Settings").assertIsDisplayed()
    // Check if the "Profile" text is displayed
    composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
    // Check if the username is displayed
    composeTestRule.onNodeWithText("No name").assertIsDisplayed()
    // Check if the "Total Steps" text is displayed
    composeTestRule.onNodeWithText("Total Steps: 0").assertIsDisplayed()
    // Check if the "Achievements" text is displayed and clickable
    composeTestRule.onNodeWithText("Achievements: 5").apply { assertIsDisplayed() }
    // Check if the "Friends List" button is displayed and has click action
    composeTestRule.onNodeWithText("Friends List").apply {
      assertIsDisplayed()
      assertHasClickAction()
    }
  }
}
