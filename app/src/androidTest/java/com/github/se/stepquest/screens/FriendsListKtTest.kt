package com.github.se.stepquest.screens

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.se.stepquest.Friend
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.theme.StepQuestTheme
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FriendsListKtTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions
  private lateinit var context: Context
  private lateinit var database: FirebaseDatabase
  private lateinit var friendsRef: DatabaseReference

  @Before
  fun setup() {
    navigationActions = mockk(relaxed = true)
    context = mockk(relaxed = true)
    database = mockk(relaxed = true)
    friendsRef = mockk(relaxed = true)
    every { database.reference } returns
        mockk {
          every { child(any()) } returns
              mockk {
                every { child(any()) } returns mockk { every { child(any()) } returns friendsRef }
              }
        }
  }

  @Test
  fun everything_is_displayed() {
    composeTestRule.setContent {
      StepQuestTheme { FriendsListScreen(navigationActions = navigationActions, "testUserId") }
    }
    composeTestRule.onNodeWithText("No friends yet").assertExists()
  }

  @Test
  fun display_with_friends() {

    val fakeFriendsList =
        listOf(
            //ORIGINAL:
            //Friend("Alice", null, true),
            //Friend("Bob", null, false),
            //Friend("Charlie", null, true),
            //Friend("David", null, false),
            Friend("Alice", true),
            Friend("Bob", false),
            Friend("Charlie", true),
            Friend("David", false),
        )
    composeTestRule.setContent {
      StepQuestTheme {
        FriendsListScreen(
            navigationActions = navigationActions,
            "testUserId",
            testCurrentFriendsList = fakeFriendsList)
      }
    }

    composeTestRule.onNodeWithText("Friends").assertIsDisplayed()
    fakeFriendsList.forEach { friend ->
      composeTestRule.onNodeWithText(friend.name).assertIsDisplayed()
    }
    composeTestRule.onNodeWithText("Alice").performClick()
    composeTestRule.onNodeWithText("Alice").assertExists()
  }

  @Test
  fun data_base_friends() {
    //ORIGINAL: val fakeFriendsList = mutableListOf(Friend("Alice", null, true), Friend("Bob", null, false))
      val fakeFriendsList = mutableListOf(Friend("Alice", true), Friend("Bob", false))

      every { friendsRef.addListenerForSingleValueEvent(any()) } answers
        {
          val listener = arg<ValueEventListener>(0)
          listener.onDataChange(
              mockk { every { getValue<List<Friend>>()?.toMutableList() } returns fakeFriendsList })
        }
    composeTestRule.setContent {
      StepQuestTheme {
        FriendsListScreen(
            navigationActions = navigationActions,
            "testUserId",
            testCurrentFriendsList = fakeFriendsList)
      }
    }
    composeTestRule.onNodeWithText("Friends").assertIsDisplayed()
    fakeFriendsList.forEach { friend ->
      composeTestRule.onNodeWithText(friend.name).assertIsDisplayed()
    }
  }
}
