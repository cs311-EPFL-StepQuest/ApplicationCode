package com.github.se.stepquest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.theme.StepQuestTheme
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun everythingIsDisplayed() {

    composeTestRule.setContent {
      StepQuestTheme { LoginPage(NavigationActions(rememberNavController())) }
    }

    // Application logo is displayed
    composeTestRule.onNodeWithTag("App logo").assertIsDisplayed()
    composeTestRule.onNodeWithTag("App logo").assertHasNoClickAction()

    // Both buttons are displayed and clickable
    composeTestRule.onNodeWithText("Log in").assertIsDisplayed()
    composeTestRule.onNodeWithText("Log in").assertHasClickAction()
    composeTestRule.onNodeWithText("New player").assertIsDisplayed()
    composeTestRule.onNodeWithText("New player").assertHasClickAction()
  }
}
