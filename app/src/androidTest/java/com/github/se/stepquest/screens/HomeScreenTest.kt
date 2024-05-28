package com.github.se.stepquest.screens

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.github.se.stepquest.ui.navigation.NavigationActions
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private val mockNavigationActions = mockk<NavigationActions>(relaxed = true)
  private val context: Context = ApplicationProvider.getApplicationContext()

  @Test
  fun testHomeScreenWhenOnline() {
    // Arrange
    val homeViewModel = HomeViewModel()
    val testState =
        HomeScreenState(
            isOnline = true,
            userScore = 1500,
            currentPosition = 2,
            leaderboard = listOf("Alice" to 2000, "Bob" to 1800, "Charlie" to 1500))
    homeViewModel._state.value = testState

    // Act
    composeTestRule.setContent {
      HomeScreen(
          navigationActions = mockNavigationActions,
          userId = "test",
          context = context,
          viewModel = homeViewModel)
    }
    homeViewModel._state.value = testState

    // Assert
    composeTestRule.onNodeWithText("Challenges").assertIsDisplayed()
    composeTestRule.onNodeWithText("Leaderboard").assertIsDisplayed()
    /*composeTestRule.onNodeWithText("1. Alice : 2000").assertIsDisplayed()
    composeTestRule.onNodeWithText("2. Bob : 1800").assertIsDisplayed()
    composeTestRule.onNodeWithText("3. Charlie : 1500").assertIsDisplayed()
    composeTestRule.onNodeWithText("Your current score is 1500").assertIsDisplayed()*/
  }

  @Test
  fun testHomeScreenWhenOffline() {
    // Arrange
    val homeViewModel = HomeViewModel()
    val testState = HomeScreenState(isOnline = false)
    homeViewModel._state.value = testState

    // Act
    composeTestRule.setContent {
      HomeScreen(
          navigationActions = mockNavigationActions,
          userId = "test",
          context = context,
          viewModel = homeViewModel)
    }
    homeViewModel._state.value = testState

    // Assert
    /*composeTestRule.onNodeWithText("No challenges available").assertIsDisplayed()
    composeTestRule.onNodeWithText("Leaderboard is not available").assertIsDisplayed()*/
  }

  @Test
  fun testChallengeCompletionPopUp() {
    // Arrange
    val homeViewModel = HomeViewModel()
    val testState = HomeScreenState(isOnline = true, showChallengeCompletionPopUp = true)
    homeViewModel._state.value = testState

    // Act
    composeTestRule.setContent {
      HomeScreen(
          navigationActions = mockNavigationActions,
          userId = "test",
          context = context,
          viewModel = homeViewModel)
    }
    homeViewModel._state.value = testState

    // Assert
    composeTestRule
        .onNodeWithText("Congratulations! You have completed some challenges!")
        .assertIsDisplayed()
  }
}
