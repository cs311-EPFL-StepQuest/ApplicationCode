package com.github.se.stepquest.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.github.se.stepquest.ProgressionPage
import com.github.se.stepquest.TestUserRepository
import org.junit.Rule
import org.junit.Test

class ProgressionPageTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun everythingIsDisplayed() {
    composeTestRule.setContent { ProgressionPage(TestUserRepository()) }
    composeTestRule.onNodeWithTag("CharacterImage").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Daily steps icon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Daily steps text").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Weekly steps icon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Weekly steps text").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Bosses defeated icon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Bosses defeated text").assertIsDisplayed()

    val button = composeTestRule.onNodeWithTag("SetNewGoalButton")
    button.assertIsDisplayed()
  }
}
