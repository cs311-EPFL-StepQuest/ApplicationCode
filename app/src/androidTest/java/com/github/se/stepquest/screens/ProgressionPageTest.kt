package com.github.se.stepquest.screens

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.github.se.stepquest.ProgressionPage
import com.github.se.stepquest.TestUserRepository1
import com.github.se.stepquest.TestUserRepository2
import com.github.se.stepquest.services.getCachedSteps
import com.github.se.stepquest.services.isOnline
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProgressionPageTest {

  private lateinit var context: Context

  @get:Rule val composeTestRule = createComposeRule()

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
  }

  @Test
  fun everythingIsDisplayed() {
    composeTestRule.setContent { ProgressionPage(TestUserRepository1(), context) }
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

  @Test
  fun dailyGoalAchievementTest() {
    composeTestRule.setContent { ProgressionPage(TestUserRepository2(), context) }
    composeTestRule.onNodeWithTag("main congratulation dialog text").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Confirm button").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Confirm button").performClick()
  }

  @Test
  fun stepGoalSetTest() {
    composeTestRule.setContent { ProgressionPage(TestUserRepository1(), context) }
    composeTestRule.onNodeWithTag("SetNewGoalButton").performClick()
  }

  @Test
  fun offlineStatsTest() {
    mockkStatic("com.github.se.stepquest.services.CacheFunctionsKt")
    every { isOnline(any()) } returns false
    every { getCachedSteps(any()) } returns 1000
    composeTestRule.setContent { ProgressionPage(TestUserRepository1(), context) }
    composeTestRule.onNodeWithTag("Steps taken since offline text").assertIsDisplayed()
  }
}
