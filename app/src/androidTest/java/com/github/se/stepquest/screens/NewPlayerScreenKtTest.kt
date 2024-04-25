package com.github.se.stepquest.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.mockk.*
import io.mockk.junit4.MockKRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewPlayerScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  private lateinit var firebaseAuth: FirebaseAuth
  private lateinit var database: FirebaseDatabase

  @Before
  fun setUp() {
    firebaseAuth = mockk(relaxed = true)
    database = mockk(relaxed = true)
    every {
      database.reference.child(any()).child(any()).addListenerForSingleValueEvent(any())
    } just Runs
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun givenNewPlayer_whenScreenDisplayed_thenUsernameTextFieldDisplayed() {
    every {
      database.reference.child(any()).child(any()).addListenerForSingleValueEvent(any())
    } answers
        {
          val callback = arg<ValueEventListener>(1)
          callback.onDataChange(mockk())
        }

    composeTestRule.setContent { NewPlayerScreen(navigationActions = mockk(), context = mockk()) }

    composeTestRule.onNodeWithText("Username").assertExists()
  }

  @Test
  fun givenExistingPlayer_whenScreenDisplayed_thenLogInButtonDisplayed() {
    every {
      database.reference.child(any()).child(any()).addListenerForSingleValueEvent(any())
    } answers
        {
          val callback = arg<ValueEventListener>(1)
          callback.onDataChange(
              mockk { every { getValue(String::class.java) } returns "existingUsername" })
        }

    composeTestRule.setContent { NewPlayerScreen(navigationActions = mockk(), context = mockk()) }

    composeTestRule.onNodeWithText("You already have an account.").assertExists()
    composeTestRule.onNodeWithText("Log in").assertExists()
  }
}
