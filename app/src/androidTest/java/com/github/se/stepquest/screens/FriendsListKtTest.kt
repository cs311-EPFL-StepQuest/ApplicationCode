package com.github.se.stepquest.screens

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.github.se.stepquest.Friend
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.theme.StepQuestTheme
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FriendsListKtTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions
  private lateinit var context: Context

  @Before
  fun setup() {
    navigationActions = mockk(relaxed = true)
    context = mockk(relaxed = true)
  }

  @Test
  fun everything_is_displayed() {

    composeTestRule.setContent {
      StepQuestTheme { FriendsListScreen(navigationActions = navigationActions) }
    }
    composeTestRule.onNodeWithText("No friends yet").assertExists()
  }

  @Test
  fun display_with_friends() {

    val fakeFriendsList =
        listOf(
            Friend("Alice", "https://example.com/alice.jpg", true),
            Friend("Bob", "https://example.com/bob.jpg", false),
            Friend("Charlie", "https://example.com/charlie.jpg", true),
            Friend("David", "https://example.com/david.jpg", false),
        )
    composeTestRule.setContent {
      StepQuestTheme {
        FriendsListScreen(
            navigationActions = navigationActions, testCurrentFriendsList = fakeFriendsList)
      }
    }

    composeTestRule.onNodeWithText("Friends").assertIsDisplayed()
    fakeFriendsList.forEach { friend ->
      composeTestRule.onNodeWithText(friend.name).assertIsDisplayed()
    }
  }
}
