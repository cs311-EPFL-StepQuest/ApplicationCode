package com.github.se.stepquest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.stepquest.ui.theme.StepQuestTheme
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun everythingIsDisplayed() {

    composeTestRule.setContent { StepQuestTheme { MyAppNavHost() } }

    // Application logo is displayed
    composeTestRule.onNodeWithTag("App logo").assertExists("The app logo doesn't exist?!")
    composeTestRule.onNodeWithTag("App logo").assertHasNoClickAction()

    // Both buttons are displayed and clickable
    composeTestRule.onNodeWithText("Log in").assertExists("The login button doesn't exist?!")
    composeTestRule.onNodeWithText("Log in").assertHasClickAction()
    composeTestRule
        .onNodeWithText("New player")
        .assertExists("The new player button doesn't exist?!")
    composeTestRule.onNodeWithText("New player").assertHasClickAction()
  }

  @Test
  fun buttonsAreClickable() {

    composeTestRule.setContent { StepQuestTheme { MyAppNavHost() } }
  }
}
